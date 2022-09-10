package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.service.UserService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码---这里只是发送到redis，发送到手机得要钱啦
     * @param phone
     * @return
     */
    @GetMapping("/send")
    public R<String> sendCode(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    /**
     * 登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Map map){
        return userService.login(map);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        return userService.loginout(request);
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public R<User> getUserInfo(){
        User userInfo = userService.getById(ThreadLocalUtil.get());
        return userInfo!=null?R.success(userInfo):R.error("获取用户信息失败");
    }
}
