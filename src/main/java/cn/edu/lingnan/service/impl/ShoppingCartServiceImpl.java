package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.ShoppingCart;
import cn.edu.lingnan.mapper.ShoppingCartMapper;
import cn.edu.lingnan.service.ShoppingCartService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public R<List> listCart() {
        //1.获取用户id
        Long userId = ThreadLocalUtil.get();
        //2.根据用户id查询
        List<ShoppingCart> shoppingCarts = list(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));
        return R.success(shoppingCarts);
    }

    @Override
    public R<ShoppingCart> add(ShoppingCart shoppingCart) {
        //获取userId
        Long userId = ThreadLocalUtil.get();
        shoppingCart.setUserId(userId);
        //查询数据库是否有对应 userId 和 dishId或setmealId 的记录
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //userId
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        //dishId或setmealId
        Long dishId = shoppingCart.getDishId();
        if (dishId!=null) {
            //添加的是dish
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加的是setmeal
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart aShoppingCart = getOne(lambdaQueryWrapper);
        //判断有无记录
        if (aShoppingCart!=null) {
            //有记录,修改原记录
            Integer number = aShoppingCart.getNumber();
            aShoppingCart.setNumber(number + 1);
            updateById(aShoppingCart);
        }else {
            //无记录,插入
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            save(shoppingCart);
            aShoppingCart = shoppingCart;
        }
        return R.success(aShoppingCart);
    }

    @Override
    public R<ShoppingCart> sub(ShoppingCart shoppingCart) {
        //获取userId
        Long userId = ThreadLocalUtil.get();
        shoppingCart.setUserId(userId);
        //查询数据库是否有对应 userId 和 dishId或setmealId 的记录
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //userId
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        //dishId或setmealId
        Long dishId = shoppingCart.getDishId();
        if (dishId!=null) {
            //减少的是dish
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //减少的是setmeal
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart aShoppingCart = getOne(lambdaQueryWrapper);
        //判断有无记录
        if (aShoppingCart!=null && aShoppingCart.getNumber()>1) {
            //有记录,修改原记录
            Integer number = aShoppingCart.getNumber();
            aShoppingCart.setNumber(number - 1);
            updateById(aShoppingCart);
        }else if(aShoppingCart==null || aShoppingCart.getNumber()==1){
            //删除记录
            removeById(aShoppingCart);
            aShoppingCart.setNumber(0);
        }
        return R.success(aShoppingCart);
    }

    @Override
    public R<String> clean() {
        //获取userId
        Long userId = ThreadLocalUtil.get();
        //删除所有userId的记录
        boolean isSuccess = remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));
        if (!isSuccess){
            return R.error("清空失败！");
        }
        return R.success("已清空！");
    }
}
