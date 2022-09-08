package cn.edu.lingnan.service;

import cn.edu.lingnan.entity.DishFlavor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    boolean removeFlavorByDishId(List<Long> ids);
}
