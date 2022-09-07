package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.DishDto;
import cn.edu.lingnan.entity.Category;
import cn.edu.lingnan.entity.Dish;
import cn.edu.lingnan.entity.DishFlavor;
import cn.edu.lingnan.mapper.DishMapper;
import cn.edu.lingnan.service.CategoryService;
import cn.edu.lingnan.service.DishFlavorService;
import cn.edu.lingnan.service.DishService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public R<Page> list(Integer page, Integer pageSize, String name) {
        log.info("获取菜品，第{}页的{}个",page,pageSize);
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        page(pageInfo,new LambdaQueryWrapper<Dish>().like(StringUtils.isNotEmpty(name), Dish::getName, name));
        Page<DishDto> dishDtoPageInfo = new Page<>();

        BeanUtil.copyProperties(pageInfo,dishDtoPageInfo,"records");

        List<Dish> dishList = pageInfo.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtil.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(dishDtoList);

        if (dishDtoPageInfo==null) {
            return R.error("查询失败！");
        }
        return R.success(dishDtoPageInfo);
    }

    @Override
    public R<DishDto> getOneById(Long id) {
        Dish dish = getById(id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, id));
        DishDto dishDto = BeanUtil.copyProperties(dish, DishDto.class);
        dishDto.setFlavors(dishFlavorList);
        return R.success(dishDto);
    }

    @Override
    public R<String> saveWithDishDto(DishDto dishDto) {
        boolean isSuccessDish = save(dishDto);
        if (!isSuccessDish) {
            return R.error("添加菜品失败！");
        }
        List<DishFlavor> flavors = dishDto.getFlavors().stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishDto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        boolean isSuccessFlavor = dishFlavorService.saveBatch(flavors);
        if (!isSuccessFlavor) {
            return R.error("添加菜品口味失败！");
        }
        return R.success("添加菜品成功！");
    }

    @Override
    public R<String> updateWithDishDto(DishDto dishDto) {
        boolean isSuccessDish = updateById(dishDto);
        if (!isSuccessDish) {
            return R.error("修改菜品信息失败！");
        }
        List<DishFlavor> flavors = dishDto.getFlavors();
        boolean isSuccessFlavor = dishFlavorService.updateBatchById(flavors);
        if (!isSuccessFlavor) {
            return R.error("修改菜品口味失败！");
        }
        return R.success("修改菜品信息成功！");
    }

    @Override
    public R<String> updateStatusBatchByIds(Integer status, long[] ids) {
        Integer count = dishMapper.updateStatusBatchByIds(status, ids);
        if (count==0){
            return R.error(status==0?"停售失败！":"启售失败！");
        }
        return R.success(status==0?"停售成功！":"启售成功！");
    }
}
