package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.DishFlavor;
import cn.edu.lingnan.mapper.DishFlavorMapper;
import cn.edu.lingnan.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
