package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

public interface EmployeeService extends IService<Employee> {
    R<Employee> login(Employee employee);   //IService<Employee>:mybatisPlus的规范
}
