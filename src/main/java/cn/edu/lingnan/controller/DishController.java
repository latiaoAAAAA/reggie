package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.service.DishService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;

    @GetMapping("/page")
    public R<Page> list(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "name",required = false) String name){
        return dishService.list(page,pageSize,name);
    }
}
