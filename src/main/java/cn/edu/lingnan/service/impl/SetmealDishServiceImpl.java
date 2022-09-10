package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.SetmealDish;
import cn.edu.lingnan.mapper.SetmealDishMapper;
import cn.edu.lingnan.service.SetmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 批量删除套餐时 删除其对应菜品
     * @param ids 套餐id
     * @return int
     */
    @Override
    public boolean removeSetmealDishByDishId(List<Long> ids) {
        Integer count = setmealDishMapper.removeSetmealDishByDishId(ids);
        log.info("删除套餐{}及其对应菜品",ids.toString());
        return (count==0 || count==null)?false:true;
    }
}
