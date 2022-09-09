package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.OrdersDto;
import cn.edu.lingnan.entity.Orders;
import cn.edu.lingnan.service.OrderService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param ordersDto
     * @return
     */
    @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody OrdersDto ordersDto){
        return orderService.submitOrder(ordersDto);
    }

    /**
     * 分页获取订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> getOrders(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize){
        return orderService.getOrders(page,pageSize);
    }

    /**
     * 再下一单
     * @param map
     * @return
     */
    @PostMapping("/again")
    public R<String> submitAgainOrder(@RequestBody Map<String,Long> map){
        return orderService.submitAgainOrder(map);
    }
}
