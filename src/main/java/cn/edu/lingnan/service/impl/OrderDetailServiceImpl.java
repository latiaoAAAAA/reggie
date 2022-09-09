package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.OrderDetail;
import cn.edu.lingnan.mapper.OrderDetailMapper;
import cn.edu.lingnan.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
