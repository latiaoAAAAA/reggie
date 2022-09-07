package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import cn.edu.lingnan.mapper.EmployeeMapper;
import cn.edu.lingnan.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public R<Employee> login(HttpServletRequest request,Employee employee) {
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
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }  //ServiceImpl<EmployeeMapper, Employee>:mybatisPlus的规范

//    @Override
//    public R<Employee> login(Employee employee) {
//        //    1.将页面提交的密码password进行md5加密；
//        String password = employee.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        //    2.根据页面提交的用户名username查询数据库；
//        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getUsername,employee.getUsername());
//        Employee emp = getOne(queryWrapper);
//        //    3.如果没有查询到用户名则返回登录失败结果；
//        if(emp==null){
//            return R.error("登录失败");
//        }
//        //    4.密码对比，如果不一致则返回登录失败结果；
//        if(!password.equals(emp.getPassword())){
//            return R.error("密码错误");
//        }
//        //    5.查看员工状态，如果为已禁用状态，则返回员工已禁用状态结果；
//        if(emp.getStatus()==0){
//            return R.error("用户名已禁用");
//        }
//        //    6.登录成功，将员工放入ThreadLocal和Redis并返回登录成功结果；
//        String key = LOGIN_EMPLOYEE_DATA+emp.getId().toString();
//        String token = UUID.randomUUID().toString(true);
//        String tokenKey = LOGIN_EMPLOYEE_TOKEN+emp.getId().toString();
//        ThreadLocalUtil.put(emp);
//        Map<String, Object> empMap = BeanUtil
//                .beanToMap(emp,
//                        new HashMap<>(),
//                        CopyOptions.create()
//                                .setIgnoreNullValue(true)
//                                .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString())
//                );
//        stringRedisTemplate.opsForHash().putAll(key,empMap);
//        stringRedisTemplate.opsForValue().set(tokenKey,token,1L,TimeUnit.DAYS);
//        stringRedisTemplate.expire(key,1L, TimeUnit.DAYS);
//        return R.success(emp).add("token",token);
//    }  //ServiceImpl<EmployeeMapper, Employee>:mybatisPlus的规范

    @Override
    public R<String> save(HttpServletRequest request, Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        //1.记录日志
        log.info("新增员工，员工信息：{}",employee.toString());
        //2.设置初始密码 及 其他信息
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        boolean isSuccess = save(employee);
        if (!isSuccess) {
            return R.error("添加员工失败！");
        }
        return R.success("添加员工成功！");
    }

    @Override
    public R<Page> list(Integer page, Integer pageSize,String name) {
        log.info("分页获取员工信息，第{}页",page.toString());
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        page(pageInfo, new LambdaQueryWrapper<Employee>()
                .like(StringUtils.isNotEmpty(name), Employee::getName, name)
                .orderByDesc(Employee::getCreateTime));
        if (pageInfo==null) {
            return R.error("获取失败！");
        }
        return R.success(pageInfo);
    }

    @Override
    public R<Employee> getOneById(Long id) {
        log.info("获取需要修改员工信息，ID：{}",id.toString());
        Employee employee = getById(id);
        if (employee ==null) {
            return R.error("查询失败！");
        }
        return R.success(employee);
    }

    @Override
    public R<String> update(HttpServletRequest request, Employee employee) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        //1.记录日志
        log.info("修改员工信息：{}",employee.toString());
        //2.设置初始密码 及 其他信息
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        boolean isSuccess = updateById(employee);
        if (!isSuccess) {
            return R.error("修改员工信息失败！");
        }
        return R.success("修改员工信息成功！");
    }
}
