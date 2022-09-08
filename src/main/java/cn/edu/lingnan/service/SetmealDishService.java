package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    boolean removeSetmealDishByDishId(List<Long> ids);
}
