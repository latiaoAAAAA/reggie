package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.dto.SetmealDto;
import cn.edu.lingnan.service.SetmealService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页获取套餐数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> list(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "name",required = false) String name){
        return setmealService.list(page,pageSize,name);
    }

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        return setmealService.saveWithSetmealDto(setmealDto);
    }

    /**
     * 套餐信息回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getOneById(@PathVariable("id") Long id){
        return setmealService.getOneById(id);
    }

    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        return setmealService.updateWithSetmealDto(setmealDto);
    }

    /**
     * 批量停售或起启售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatusBatchByIds(@PathVariable Integer status,@RequestParam("ids") long[] ids){
        return setmealService.updateStatusBatchByIds(status,ids);
    }

    /**
     * 批量删除套餐数据
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteByIds(@RequestParam("ids") List<Long> ids){
        return setmealService.removeDishAntFlavorByIds(ids);
    }

    /**
     * 根据categoryId获取套餐---用户端展示
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public R<List> listByCategoryId(@RequestParam("categoryId") Long categoryId, @RequestParam("status") Integer status){
        return setmealService.listByCategoryId(categoryId,status);
    }

    /**
     * 根据setmealId获取套餐内dish---用户端展示
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List> listSetmealDishBySetmealId(@PathVariable("id") Long id){
        return setmealService.listSetmealDishBySetmealId(id);
    }
}
