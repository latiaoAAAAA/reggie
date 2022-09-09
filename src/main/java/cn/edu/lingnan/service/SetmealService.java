package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.SetmealDto;
import cn.edu.lingnan.entity.Setmeal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    R<Page> list(Integer page, Integer pageSize, String name);

    R<String> saveWithSetmealDto(SetmealDto setmealDto);

    R<SetmealDto> getOneById(Long id);

    R<String> updateWithSetmealDto(SetmealDto setmealDto);

    R<String> updateStatusBatchByIds(Integer status, long[] ids);

    R<String> removeDishAntFlavorByIds(List<Long> ids);

    R<List> listByCategoryId(Long categoryId, Integer status);

    R<List> listSetmealDishBySetmealId(Long id);
}
