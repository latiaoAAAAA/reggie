package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Employee;
import cn.edu.lingnan.service.EmployeeService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录 Employee
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee){
        return employeeService.login(employee);
    }

    /**
     * 员工退出登录 Employee
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(){
        // request.getSession().removeAttribute("employee");
        log.info("员工退出登录,id:{}",ThreadLocalUtil.get());
        ThreadLocalUtil.remove();
        return R.success("已退出登录");
    }

    /**
     * 添加员工 Employee
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        return employeeService.saveOne(employee);
    }

    /**
     * 分页获取员工列表 Employee
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> list(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "name",required = false) String name){
        return employeeService.list(page,pageSize,name);
    }

    /**
     * 根据id获取员工---修改数据时回显 Employee
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getOneById(@PathVariable Long id){
        return employeeService.getOneById(id);
    }

    /**
     * 修改员工信息 Employee
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        return employeeService.updateOne(employee);
    }
}
