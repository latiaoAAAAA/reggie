package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.DishDto;
import cn.edu.lingnan.entity.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {
    R<Page> list(Integer page, Integer pageSize, String name);

    R<DishDto> getOneById(Long id);

    R<String> saveWithDishDto(DishDto dishDto);

    R<String> updateWithDishDto(DishDto dishDto);

    R<String> updateStatusBatchByIds(Integer status, long[] ids);
}
