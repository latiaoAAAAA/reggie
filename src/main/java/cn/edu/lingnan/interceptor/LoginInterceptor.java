package cn.edu.lingnan.interceptor;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

import static cn.edu.lingnan.constants.RedisConstants.LOGIN_USER_TOKEN;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    //在拦截器中需要用到stringRedisTemplate，但拦截器没有交由spring容器管理，故无法通过@Autowired注入
    //但注册拦截器的配置类是交由spring容器管理的，所以可以通过构造方法获取stringRedisTemplate
    private StringRedisTemplate stringRedisTemplate;
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取front请求头中的token
        String token = request.getHeader("Authorization");
        //验证
        if (request.getSession().getAttribute("employee")!=null){
            log.info("已登录，ID为{}",request.getSession().getAttribute("employee"));
            ThreadLocalUtil.put((Long) request.getSession().getAttribute("employee"));
            return true;
        }else if (token !=null ||
                StrUtil.isNotBlank(token)){
            //对比redis(redis中有，且有值，才放行)
            if (stringRedisTemplate.hasKey(LOGIN_USER_TOKEN + token)) {
                //获取userId
                String userId = stringRedisTemplate.opsForValue().get(LOGIN_USER_TOKEN + token);
                if(userId!=null||StrUtil.isNotBlank(userId)){
                    log.info("token:{},userId:{}", token,userId);
                    //重置有效时间
                    stringRedisTemplate.expire(LOGIN_USER_TOKEN + token,1L, TimeUnit.DAYS);
                    //存入ThreadLocal
                    ThreadLocalUtil.put(Long.valueOf(userId));
                    return true;
                }
            }
        }
        log.info("未登录或登录已过期");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
