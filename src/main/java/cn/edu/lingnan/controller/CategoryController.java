package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Category;
import cn.edu.lingnan.service.CategoryService;
import cn.edu.lingnan.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类 Category
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        Long employeeId = ThreadLocalUtil.get();
        boolean isSuccess = categoryService.save(category);
        log.info("新增菜品分类,菜品分类信息：{}",category.toString());
        return isSuccess?R.success("新增菜品或套餐分类成功!"):R.error("新增菜品或套餐分类失败！");
    }

    /**
     * 分页获取 菜品或套餐管理 Category
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){
        return categoryService.list(page,pageSize);
    }

    /**
     * 修改分类 Category
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        return categoryService.update(category);
    }

    /**
     * 删除分类 Category
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long ids){
        return categoryService.delete(ids);
    }

    /**
     * 获取分类列表(菜品或套餐) Category
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List> listType(@RequestParam(value = "type",required = false) Integer type){
        return categoryService.listType(type);
    }
}
