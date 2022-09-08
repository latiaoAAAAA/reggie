import cn.edu.lingnan.entity.Employee;
import cn.edu.lingnan.entity.User;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static cn.edu.lingnan.constants.RedisConstants.LOGIN_USER_CODE;

public class test {

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

}
