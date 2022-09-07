package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.CustomException;
import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Category;
import cn.edu.lingnan.entity.Dish;
import cn.edu.lingnan.entity.Setmeal;
import cn.edu.lingnan.mapper.CategoryMapper;
import cn.edu.lingnan.service.CategoryService;
import cn.edu.lingnan.service.DishService;
import cn.edu.lingnan.service.SetmealService;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public R<String> save(HttpServletRequest request, Category category) {
        Long employeeId = (Long) request.getSession().getAttribute("employee");
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(employeeId);
//        category.setUpdateUser(employeeId);
        log.info("新增菜品分类,菜品分类信息：{}",category.toString());
        boolean isSuccess = save(category);
        if (!isSuccess) {
            return R.error("新增菜品或套餐分类失败！");
        }
        return R.success("新增菜品或套餐分类成功!");
    }

    @Override  //LocalDateTime传给前端时序列化成数组问题：在实体类对应字段上加@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public R<Page> list(Integer page, Integer pageSize) {
        log.info("获取菜品与套餐分类，第{}页的{}个",page,pageSize);
        Page<Category> pageInfo = new Page<>(page,pageSize);
        Page<Category> results = page(pageInfo, new LambdaQueryWrapper<Category>().orderByAsc(Category::getSort));
        if (results==null){
            return R.error("获取菜品与套餐分类失败！");
        }
        return R.success(results);
    }

    @Override
    public R<String> update(HttpServletRequest request, Category category) {
        log.info("修改菜品或套餐分类，原信息：{}",category.toString());
        Long employeeId = (Long) request.getSession().getAttribute("employee");
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(employeeId);
        boolean isSuccess = updateById(category);
        if (!isSuccess) {
            return R.error("修改菜品或套餐分类失败！");
        }
        return R.success("修改菜品或套餐分类成功!");
    }

    @Override
    public R<String> delete(Long ids) {
        log.info("删除菜品或套餐分类，ID：{}",ids);
        //查询是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int countDish = dishService.count(dishLambdaQueryWrapper);
        if (countDish>0) {
            throw new CustomException("存在与菜品的关联，删除失败！");
        }
        //查询是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int countSetmeal = setmealService.count(setmealLambdaQueryWrapper);
        if (countSetmeal>0) {
            throw new CustomException("存在与套餐的关联，删除失败！");
        }
        //删除
        boolean isSuccess = removeById(ids);
        if (!isSuccess) {
            return R.error("删除菜品或套餐分类失败！");
        }
        return R.success("删除菜品或套餐分类成功!");
    }

    @Override
    public R<List> list(Integer type) {
        List<Category> categoryList = list(new LambdaQueryWrapper<Category>().eq(Category::getType,type));
        if (categoryList==null || categoryList.isEmpty()) {
            return R.error("获取分类列表失败！");
        }
        return R.success(categoryList);
    }
}
