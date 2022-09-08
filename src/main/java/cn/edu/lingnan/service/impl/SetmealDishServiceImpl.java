package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.SetmealDish;
import cn.edu.lingnan.mapper.SetmealDishMapper;
import cn.edu.lingnan.service.SetmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public boolean removeSetmealDishByDishId(List<Long> ids) {
        Integer count = setmealDishMapper.removeSetmealDishByDishId(ids);
        return (count==0 || count==null)?false:true;
    }
}
