package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    R<List> listCart();

    R<ShoppingCart> add(ShoppingCart shoppingCart);

    R<ShoppingCart> sub(ShoppingCart shoppingCart);

    R<String> clean();
}
