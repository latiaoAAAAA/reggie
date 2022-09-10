package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.mapper.UserMapper;
import cn.edu.lingnan.service.UserService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.edu.lingnan.constants.RedisConstants.LOGIN_USER_CODE;
import static cn.edu.lingnan.constants.RedisConstants.LOGIN_USER_TOKEN;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public R<String> sendCode(String phone) {
        if (stringRedisTemplate.hasKey(LOGIN_USER_CODE+phone)) {
            return R.success(stringRedisTemplate.opsForValue().get(LOGIN_USER_CODE+phone));
        }
        //1.生成随机验证码
        String code = RandomUtil.randomNumbers(6);
        //2.存入redis，并返回验证码
        stringRedisTemplate.opsForValue().set(LOGIN_USER_CODE+phone,code,2L, TimeUnit.MINUTES);
        log.info("验证码:{}",code);
        return R.success(code);
    }

    @Override
    public R<String> login(Map map) {
        //1.获取输入的手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //2.获取正确的验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_USER_CODE+phone);
        //3.修改手机号 或 验证码过期 判断
        if (StringUtils.isEmpty(cacheCode)) {
            return R.error("号码错误或验证码已过期！");
        }
        //4.验证码不匹配
        if (!cacheCode.equals(code)) {
            return R.error("验证码不匹配,请重新获取！");
        }
        //5.验证通过，根据phone查询用户
        User user = query().eq(StringUtils.isNotEmpty(phone), "phone", phone).one();
//        User user = getOne(new LambdaQueryWrapper<User>().eq(StringUtils.isNotEmpty(phone), User::getPhone, phone));
        //6.用户不存在，注册新用户
        long userId = 0;
        if (BeanUtil.isEmpty(user)) {
            User registerUser = new User();
            registerUser.setName("user_"+RandomUtil.randomString(7));
            registerUser.setPhone(phone);
            registerUser.setStatus(1);
            registerUser.setAvatar("651f6c36-2b78-484c-b3d9-a8c7d24c9b94.jpg");
            boolean isSuccess = save(registerUser);
            userId = registerUser.getId();
            if (!isSuccess) {
                return R.error("注册失败!");
            }
        }else {
            userId=user.getId();
        }
        //7.用户存在
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_TOKEN+token, String.valueOf(userId),1L,TimeUnit.DAYS);
        log.info("用户注册:{}",user.toString());
        return R.success(token);
    }

//    @Override
//    public Result login(LoginFormDTO loginForm, HttpSession session) {
//        //1.验证手机号（RegexUtils在utils包下，用于正则表达式的验证）
//        //1.1获取手机号
//        String phone = loginForm.getPhone();
//        //1.2验证格式
//        if (RegexUtils.isPhoneInvalid(phone)) {
//            //2.1验证不通过,返回错误信息
//            return Result.fail("手机号格式错误！");
//        }
//        //1.3校验验证码
//        //TODO Object cacheCode = session.getAttribute("code");  //这是之前存于session的验证码
//        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);  //从redis中取出验证码
//        String code = loginForm.getCode();  //这是用户输入的验证码
//        if (cacheCode==null || !cacheCode.equals(code)) {
//            //2.2验证码不一致，返回错误信息
//            return Result.fail("验证码错误！");
//        }
//        //3.验证通过，根据手机号查询用户(select * from tb_user where phone=?)
//        User user = query().eq("phone",phone).one();
//        //4.用户不存在，则创建一个新用户，并保存于数据库中
//        if (user==null) {
//            user = createUserWithPhone(phone);
//        }
//        // //5.用户存在，则保存到session中(BeanUtil为hutool的工具类，将user所有参数复制到userDTO中)
//        //session.setAttribute("user", userDTO);
//        // TODO 5.用户存在，则保存到Redis中(BeanUtil为hutool的工具类，将user所有参数复制到userDTO中)
//        //5.1随机生成token，作为登录令牌(这里不用JWT，用UUID)
//        String token = UUID.randomUUID().toString(true);
//        //5.2将user对象转为HashMap
//        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO,new HashMap<>(),
//                CopyOptions.create()
//                        .setIgnoreNullValue(true)  //忽略空值
//                        .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString())  //value转成String类型
//        );
//        //5.3存入redis中
//        String tokenKey = LOGIN_USER_KEY+token;
//        stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
//        //5.4设置token有效期
//        stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL,TimeUnit.MINUTES);
//        // TODO 6.返回
//        return Result.ok(token);
//    }

    /**
     * 根据id获取userName，订单业务需要用到
     * @param id
     * @return
     */
    @Override
    public String getUserNameById(Long id) {
        User one = query().eq("id", id).eq("status", 1).one();
        return one.getName();
    }

    @Override
    @Transactional
    public R<String> loginout(HttpServletRequest request) {
        String token = request.getHeader("UAuthorization");
        Boolean isSuccess = false;
        if (stringRedisTemplate.hasKey(LOGIN_USER_TOKEN + token)) {
            isSuccess = stringRedisTemplate.delete(LOGIN_USER_TOKEN + token);
            ThreadLocalUtil.remove();
        }
        log.info("退出登录");
        return isSuccess?R.success("退出成功"):R.error("退出失败");
    }
}