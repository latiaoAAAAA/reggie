package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/send")
    public R<String> sendCode(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody Map map){
        return userService.login(map);
    }
}
