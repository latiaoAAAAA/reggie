package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import cn.edu.lingnan.mapper.EmployeeMapper;
import cn.edu.lingnan.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
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
        //    6.登录成功，将员工id存入session并返回登录成功结果；

        return R.success(emp);
    }  //ServiceImpl<EmployeeMapper, Employee>:mybatisPlus的规范
}
