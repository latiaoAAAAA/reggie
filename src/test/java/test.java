import cn.edu.lingnan.ReggieApplication;
import cn.edu.lingnan.entity.Employee;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.EmployeeService;
import cn.edu.lingnan.utils.RedisUtils;
import cn.hutool.core.bean.BeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.lingnan.constants.RedisConstants.EMPLOYEE_DATA;

@SpringBootTest(classes = ReggieApplication.class)
public class test {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EmployeeService employeeService;

    @Test  //所有employee数据存入redis
    void addAllEmpToRedis(){
        List<Employee> employees = employeeService.list();
        long count = employees.stream().map(employee -> {
            Map<String, Object> empMap = RedisUtils
                    .BeanToMap(employee, "password", "updateTime", "createUser", "updateUser");
            stringRedisTemplate.opsForHash().putAll(EMPLOYEE_DATA + employee.getId(), empMap);
            System.out.println("所有employee存入redis:"+empMap);
            return empMap;
        }).count();
        System.out.println("所有employee存入redis:共"+count+"条");
    }

    @Test
    void test1(){
        Employee employee = new Employee();
        employee.setId(100L);
        employee.setName("张三");
        employee.setUsername("sange");
        employee.setPassword("123456");
//        System.out.println("BeanUtil.beanToMap(employee) = " + BeanUtil.beanToMap(employee));
//        System.out.println(BeanUtil.beanToMap(employee, new HashMap<>(), CopyOptions.create().setIgnoreNullValue(true)));
//        BeanUtil.beanToMap(
//                employee,
//                new HashMap<>(),
//                CopyOptions.create()
//                        .setIgnoreNullValue(true)
//                        .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString())
//        );
    }

    @Test
    void test4() {
        long[] ids = {123L,456L,789L};
        System.out.println("Arrays.toString(ids) = " + Arrays.toString(ids));
        String s = Arrays.toString(ids);
        System.out.println("s = " + s);
    }

    @Test
    void test5() {
        List<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        strings.add("ghi");
        strings.forEach(name->{
            System.out.print(name+" ");
        });
        String s = strings.toString();
    }

    @Test
    void test6() {
        User user = new User();
        System.out.println("BeanUtil.isEmpty(user) = " + BeanUtil.isEmpty(user));
        user.setId(111L);
        System.out.println("user = " + user);
    }

    @Test
    void test7(){
        Employee employee = new Employee();
        employee.setId(100L);
        employee.setName("张三");
        employee.setUsername("sange");
        employee.setPassword("123456");
        employee.setStatus(1);
        Map<String, Object> map = RedisUtils.BeanToMap(employee, "status","createTime","updateTime","createUser","updateUser","phone","sex","idNumber");
        System.out.println("map = " + map.toString());
    }

    @Test
    void test8() {
        ArrayList<Integer> strs = new ArrayList<>();
        strs.add(40);
        strs.add(10);
        strs.add(30);
        strs.add(50);
        strs.add(20);

        Set<String> keys = stringRedisTemplate.keys(EMPLOYEE_DATA + "*");
        List<Employee> employeeList = keys.stream().map(key -> {
            Employee employee = BeanUtil.fillBeanWithMap(stringRedisTemplate.opsForHash().entries(key), new Employee(), true);
            return employee;
        }).sorted(Comparator.comparing(Employee::getCreateTime)).collect(Collectors.toList());
        Collections.reverse(employeeList);
//        System.err.println("employeeList = " + employeeList.toString());

        List<Employee> employeeList1 = RedisUtils.getList(
                stringRedisTemplate, EMPLOYEE_DATA + "*", Employee.class, Employee::getCreateTime,1,5);
        System.out.println("employeeList1 = " + employeeList1);
        System.out.println("employeeList1 = " + employeeList1.size());

    }
}
