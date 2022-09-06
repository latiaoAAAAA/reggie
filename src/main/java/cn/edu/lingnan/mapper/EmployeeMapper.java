package cn.edu.lingnan.mapper;

import cn.edu.lingnan.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {   //BaseMapper:mybatisPlus的规范
}
