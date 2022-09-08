package cn.edu.lingnan.mapper;

import cn.edu.lingnan.entity.SetmealDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
//    @Delete({
//            "<script>",
//                "delete ",
//                "from setmeal_dish",
//                "where setmeal_id in",
//                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
//                        "#{id}",
//                    "</foreach>",
//            "</script>"
//    })
    //逻辑删除
    @Update({
            "<script>",
                "update ",
                "setmeal_dish",
                "set is_deleted = 1",
                "where setmeal_id in",
                    "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
                        "#{id}",
                    "</foreach>",
            "</script>"
    })
    Integer removeSetmealDishByDishId(@Param("ids") List<Long> ids);
}
