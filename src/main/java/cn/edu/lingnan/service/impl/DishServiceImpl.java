package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Dish;
import cn.edu.lingnan.mapper.DishMapper;
import cn.edu.lingnan.service.DishService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Override
    public R<Page> list(Integer page, Integer pageSize, String name) {
        log.info("获取菜品，第{}页的{}个",page,pageSize);
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<Dish> results = page(pageInfo,
                new LambdaQueryWrapper<Dish>().like(StringUtils.isNotEmpty(name), Dish::getName, name));
        if (results==null) {
            return R.error("查询失败！");
        }
        return R.success(results);
    }
}
