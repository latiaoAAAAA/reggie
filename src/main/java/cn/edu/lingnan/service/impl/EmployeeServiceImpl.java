package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import cn.edu.lingnan.mapper.EmployeeMapper;
import cn.edu.lingnan.service.EmployeeService;
import cn.edu.lingnan.utils.RedisUtils;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.edu.lingnan.constants.RedisConstants.EMPLOYEE_DATA;
import static cn.edu.lingnan.constants.RedisConstants.LOGIN_EMPLOYEE_TOKEN;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public R<Employee> login(Employee employee) {
        //    1.将页面提交的密码password进行md5加密；
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //    2.根据页面提交的用户名username查询数据库；
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = getOne(queryWrapper);
        //    3.如果没有查询到用户名则返回登录失败结果；
        if(emp==null){
            return R.error("登录失败");
        }
        //    4.密码对比，如果不一致则返回登录失败结果；
        if(!password.equals(emp.getPassword())){
            return R.error("密码错误");
        }
        //    5.查看员工状态，如果为已禁用状态，则返回员工已禁用状态结果；
        if(emp.getStatus()==0){
            return R.error("用户名已禁用");
        }
        //    6.登录成功，将员工放入session并返回登录成功结果；
        // request.getSession().setAttribute("employee",emp.getId());
        //    6.登录成功，将员工放入redis；
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(LOGIN_EMPLOYEE_TOKEN+token,emp.getId().toString(),1L, TimeUnit.DAYS);
        log.info("登录成功,员工id:{}",emp.getId());
        return R.success(emp).add("Etoken",token);
    }


    @Override
    @Transactional
    public R<String> saveOne(Employee employee) {
        // Long empId = (Long) request.getSession().getAttribute("employee");
        Long empId = ThreadLocalUtil.get();
        //1.设置初始密码 及 其他信息
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setStatus(1);
        boolean isSuccess = save(employee);
        if (!isSuccess) {
            return R.error("添加员工失败！");
        }
        //2.存入redis,封装方法
        Map<String, Object> empMap = RedisUtils
                .BeanToMap(employee,"password","updateTime","createUser","updateUser");
        /* 未封装
        Map<String, Object> empMap = BeanUtil.beanToMap(employee, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreProperties("password","createTime","updateTime","createUser","updateUser")
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString())
        );
        */
        stringRedisTemplate.opsForHash().putAll(EMPLOYEE_DATA+employee.getId(),empMap);
        //3.记录日志
        log.info("新增员工信息:{},Redis存储信息:{}",employee.toString(),empMap.toString());
        return R.success("添加员工成功！");
    }

    // TODO 注:此处使用redis:第一次访问在150左右，后面都是80左右
    // TODO 注:此处使用MySQL:第一次访问在500左右，后面都是30左右(用这个吧)
    @Override
    public R<Page> list(Integer page, Integer pageSize,String name) {
        log.info("分页获取员工信息:第{}页/{}个",page.toString(),pageSize.toString());
        //1.创建page对象
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //2.条件查询
        page(pageInfo, new LambdaQueryWrapper<Employee>()
                .like(StringUtils.isNotEmpty(name), Employee::getName, name)
                .orderByDesc(Employee::getCreateTime));
        log.info("分页获取员工列表,共{}条",pageInfo.getTotal());
        /*  // 使用redis    ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        long begin = System.currentTimeMillis();
        log.info("分页获取员工信息:第{}页/{}个",page.toString(),pageSize.toString());
        //1.创建page对象
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //redis
        List<Employee> employeeList = RedisUtils
                .getList(stringRedisTemplate, EMPLOYEE_DATA + "*", Employee.class, Employee::getCreateTime,page,pageSize);
        if (!employeeList.isEmpty()){
            //redis中有数据
            pageInfo.setRecords(employeeList);
            pageInfo.setTotal(stringRedisTemplate.keys(EMPLOYEE_DATA + "*").size());
            log.info("分页获取员工信息:查询Redis中的数据");
        }else{
            //2.redis中无数据,条件查询数据库
            log.info("分页获取员工信息:查询MySQL中的数据,并存入Redis");
            page(pageInfo, new LambdaQueryWrapper<Employee>()
                    .like(StringUtils.isNotEmpty(name), Employee::getName, name)
                    .orderByDesc(Employee::getCreateTime));
            long count = pageInfo.getRecords().stream().map(employee -> {
                Map<String, Object> empMap = RedisUtils
                        .BeanToMap(employee,"password","updateTime","createUser","updateUser");
                stringRedisTemplate.opsForHash().putAll(EMPLOYEE_DATA+employee.getId(),empMap);
                log.info("分页获取员工信息:{}->Redis",empMap);
                return empMap;
            }).count();
            log.info("分页获取员工信息:存入Redis共{}条记录",count);
        }
        System.err.println("用时:"+(System.currentTimeMillis()-begin));
        */
        return R.success(pageInfo);
    }

    // TODO 注:此处使用redis:第一次访问在120左右，后面都是5左右
    // TODO 注:此处使用MySQL:第一次访问在40左右，后面都是5左右(用这个吧)
    @Override
    public R<Employee> getOneById(Long id) {
        //Employee employee = RedisUtils.getOne(stringRedisTemplate, EMPLOYEE_DATA + id, Employee.class);
        log.info("原员工信息,ID:{}",id.toString());
        Employee employee = getById(id);
        if (employee ==null) {
            return R.error("查询失败！");
        }
        return R.success(employee);
    }

    @Override
    public R<String> updateOne(Employee employee) {
        // Long empId = (Long) request.getSession().getAttribute("employee");
        Long empId = ThreadLocalUtil.get();
        //1.设置初始密码 及 其他信息
        boolean isSuccess = updateById(employee);
        //2.记录日志
        log.info("修改员工信息:{}",employee.toString());
        if (!isSuccess) {
            return R.error("修改员工信息失败！");
        }
        return R.success("修改员工信息成功！");
    }
}
