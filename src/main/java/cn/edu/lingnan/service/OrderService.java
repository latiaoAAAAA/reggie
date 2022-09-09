package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.OrdersDto;
import cn.edu.lingnan.entity.Orders;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface OrderService extends IService<Orders> {
    R<String> submitOrder(OrdersDto ordersDto);

    R<Page> getOrders(Long page, Long pageSize);

    R<String> submitAgainOrder(Map<String,Long> map);
}
