package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.DishFlavor;
import cn.edu.lingnan.mapper.DishFlavorMapper;
import cn.edu.lingnan.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Autowired
    public DishFlavorMapper dishFlavorMapper;

    @Override
    public boolean removeFlavorByDishId(List<Long> ids) {
        Integer count = dishFlavorMapper.removeFlavorByDishId(ids);
        return (count==0 || count==null)?false:true;
    }
}
