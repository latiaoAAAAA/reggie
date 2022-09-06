package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    R<Employee> login(HttpServletRequest request,Employee employee);   //IService<Employee>:mybatisPlus的规范

    R<String> save(HttpServletRequest request, Employee employee);

    R<Page> list(Integer page, Integer pageSize,String name);

    R<Employee> getOneById(Long id);

    R<String> update(HttpServletRequest request, Employee employee);
}
