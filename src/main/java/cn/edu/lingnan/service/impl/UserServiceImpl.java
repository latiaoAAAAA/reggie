package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.mapper.UserMapper;
import cn.edu.lingnan.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
