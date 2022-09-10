package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService extends IService<User> {
    R<String> sendCode(String phone);

    R<String> login(Map map);

    String getUserNameById(Long id);

    R<String> loginout(HttpServletRequest request);
}
