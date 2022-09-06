package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Category;
import cn.edu.lingnan.mapper.CategoryMapper;
import cn.edu.lingnan.service.CategoryService;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public R<String> save(HttpServletRequest request, Category category) {
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(employeeId);
        category.setUpdateUser(employeeId);
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
        Page<Category> pageInfo = new Page<>();
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
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(employeeId);
        boolean isSuccess = updateById(category);
        if (!isSuccess) {
            return R.error("修改菜品或套餐分类失败！");
        }
        return R.success("修改菜品或套餐分类成功!");
    }

    @Override
    public R<String> delete(Long ids) {
        log.info("删除菜品或套餐分类，ID：{}",ids);
        boolean isSuccess = removeById(ids);
        if (!isSuccess) {
            return R.error("删除菜品或套餐分类失败！");
        }
        return R.success("删除菜品或套餐分类成功!");
    }
}
