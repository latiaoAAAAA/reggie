package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.OrdersDto;
import cn.edu.lingnan.entity.AddressBook;
import cn.edu.lingnan.entity.OrderDetail;
import cn.edu.lingnan.entity.Orders;
import cn.edu.lingnan.mapper.OrderMapper;
import cn.edu.lingnan.service.AddressBookService;
import cn.edu.lingnan.service.OrderDetailService;
import cn.edu.lingnan.service.OrderService;
import cn.edu.lingnan.service.UserService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public R<String> submitOrder(OrdersDto ordersDto) {
        //获取userId
        Long userId = ThreadLocalUtil.get();
        //根据userId获取默认地址
        AddressBook oneAddress = addressBookService.getOne(
                new LambdaQueryWrapper<AddressBook>()
                        .eq(AddressBook::getUserId, userId)
                        .eq(AddressBook::getIsDefault, 1)
        );
        //根据userId获取userName
        String userName = userService.getUserNameById(userId);
        //装配OrdersDto
        ordersDto.setUserId(ThreadLocalUtil.get());
        ordersDto.setOrderTime(LocalDateTime.now());
        ordersDto.setCheckoutTime(LocalDateTime.now());
        ordersDto.setUserName(userName);
        ordersDto.setPhone(oneAddress.getPhone());
        ordersDto.setAddress(oneAddress.getDetail());
        ordersDto.setConsignee(oneAddress.getConsignee());
        //保存订单
        boolean isSuccess1 = save(ordersDto);
        //获取订单id
        Long ordersId = ordersDto.getId();
        //获取orderDetail
        List<OrderDetail> orderDetails = ordersDto.getOrderDetails();
        //装配orderId进orderDetail
        List<OrderDetail> orderDetailList = orderDetails.stream().map(orderDetail -> {
            orderDetail.setOrderId(ordersId);
            return orderDetail;
        }).collect(Collectors.toList());
        //保存orderDetail
        boolean isSuccess2 = orderDetailService.saveBatch(orderDetailList);
        //返回
        return isSuccess1&&isSuccess2?R.success("创建订单成功"):R.error("创建订单失败");
    }

    @Override
    public R<Page> getOrders(Long page, Long pageSize) {
        //获取当前登录用户id
        Long userId = ThreadLocalUtil.get();
        //创建page
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        page(ordersPage,new LambdaQueryWrapper<Orders>().eq(Orders::getUserId,userId));
        //创建dtoPage
        Page<OrdersDto> ordersDtoPage = new Page<>();
        //copy对象，排除records
        BeanUtil.copyProperties(ordersPage,ordersDtoPage,"records");
        //手动装配ordersDtoList
        List<OrdersDto> ordersDtoList = ordersPage.getRecords().stream().map(orders -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtil.copyProperties(orders, ordersDto);
            Long ordersId = orders.getId();
            List<OrderDetail> orderDetails = orderDetailService.list(
                    new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, ordersId)
            );
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());
        //手动装配ordersDtoPage
        ordersDtoPage.setRecords(ordersDtoList);
        return ordersPage!=null?R.success(ordersDtoPage):R.error("获取所有订单失败");
    }

    @Override
    @Transactional
    public R<String> submitAgainOrder(Map<String,Long> map) {
        //获取userId
        Long userId = ThreadLocalUtil.get();
        //获取orderId
        Long orderId = map.get("id");
        System.err.println("orderId = " + orderId.toString());
        //根据orderId查询Order
        Orders oneOrder = getOne(new LambdaQueryWrapper<Orders>().eq(Orders::getId, orderId));
        System.err.println("oneOrder = " + oneOrder.toString());
        //根据orderId查询OrderDetail
        List<OrderDetail> orderDetails = orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, orderId));
        System.err.println("orderDetails = " + orderDetails.toString());
        //装配oneOrder
        oneOrder.setId(null);
        oneOrder.setUserId(ThreadLocalUtil.get());
        oneOrder.setOrderTime(LocalDateTime.now());
        oneOrder.setCheckoutTime(LocalDateTime.now());
        System.err.println("newOneOrder = " + oneOrder.toString());
        // TODO 开始保存
        //保存订单
        boolean isSuccess1 = save(oneOrder);
        //获取新的订单id
        Long newOrderId = oneOrder.getId();
        //装配newOrderId进orderDetail
        List<OrderDetail> orderDetailList = orderDetails.stream().map(orderDetail -> {
            orderDetail.setOrderId(newOrderId);
            return orderDetail;
        }).collect(Collectors.toList());
        //保存orderDetail
        boolean isSuccess2 = orderDetailService.saveBatch(orderDetailList);
        //返回
//        return isSuccess1&&isSuccess2?R.success("再下一单成功"):R.error("再下一单失败");
        return null;
    }
}
