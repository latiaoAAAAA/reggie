package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.DishDto;
import cn.edu.lingnan.service.DishService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    public R<Page> list(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "name",required = false) String name){
        return dishService.list(page,pageSize,name);
    }

    /**
     * 添加菜品数据
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        return dishService.saveWithDishDto(dishDto);
    }

    /**
     * 菜品信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getOneById(@PathVariable("id") Long id){
        return dishService.getOneById(id);
    }

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        return dishService.updateWithDishDto(dishDto);
    }

    /**
     * 批量停售或起启售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatusBatchByIds(@PathVariable Integer status,@RequestParam("ids") long[] ids){
        return dishService.updateStatusBatchByIds(status,ids);
    }

    /**
     * 批量删除菜品数据
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam("ids") List<Long> ids){
        return dishService.removeDishAntFlavorByIds(ids);
    }

    /**
     * 管理端 添加套餐时获取菜品数据
     * 用户端 菜品列表展示与菜品详细的展示
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List> listDishByCategoryId(
            @RequestParam(value = "categoryId",required = false) Long categoryId,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "status",required = false) Integer status
    ){
        return dishService.listDishByCategoryId(categoryId,name,status);
    }
}
