package cn.edu.lingnan.dto;

import cn.edu.lingnan.entity.OrderDetail;
import cn.edu.lingnan.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
