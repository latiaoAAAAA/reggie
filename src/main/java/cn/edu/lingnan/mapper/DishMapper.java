package cn.edu.lingnan.mapper;

import cn.edu.lingnan.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    //(  123,456,789 )
    @Update({
            "<script>",
                "update",
                "dish",
                "set status = #{status}",
                "where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                        "#{id}",
                    "</foreach>",
                "and ",
                "is_deleted = 0",
            "</script>"
    })
    Integer updateStatusBatchByIds(@Param("status") Integer status, @Param("ids") long[] ids);

    @Select({
            "<script>",
                "select name ",
                "from dish",
                "where id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                        "#{id}",
                    "</foreach>",
                "and ",
                "is_deleted = 0",
                "and ",
                "status = 1",
            "</script>"
    })
    List<String> getStatusDish(@Param("ids") List<Long> ids);
}
