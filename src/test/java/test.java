import cn.edu.lingnan.entity.Employee;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;

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
}
