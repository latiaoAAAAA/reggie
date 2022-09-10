package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface EmployeeService extends IService<Employee> {
    R<Employee> login(Employee employee);

    R<String> saveOne(Employee employee);

    R<Page> list(Integer page, Integer pageSize,String name);

    R<Employee> getOneById(Long id);

    R<String> updateOne(Employee employee);
}
