package cn.edu.lingnan.mapper;

import cn.edu.lingnan.entity.Setmeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    @Update({
            "<script>",
                "update",
                "setmeal",
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
                "from setmeal",
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
    List<String> getStatusSetmeal(@Param("ids") List<Long> ids);
}
