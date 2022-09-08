package cn.edu.lingnan.mapper;

import cn.edu.lingnan.entity.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
    @Delete({
            "<script>",
                "delete ",
                "from dish_flavor",
                "where dish_id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                        "#{id}",
                    "</foreach>",
            "</script>"
    })
    Integer removeFlavorByDishId(@Param("ids") List<Long> ids);
}
