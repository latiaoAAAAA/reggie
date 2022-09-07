package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.DishDto;
import cn.edu.lingnan.service.DishService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        return dishService.saveWithDishDto(dishDto);
    }

    @GetMapping("/{id}")
    public R<DishDto> getOneById(@PathVariable("id") Long id){
        return dishService.getOneById(id);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        return dishService.updateWithDishDto(dishDto);
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatusBatchByIds(@PathVariable Integer status,@RequestParam("ids") long[] ids){
        return dishService.updateStatusBatchByIds(status,ids);
    }
}
