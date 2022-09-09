package cn.edu.lingnan.controller;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Category;
import cn.edu.lingnan.service.CategoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category){
        return categoryService.save(request,category);
    }

    /**
     * 分页获取 菜品或套餐管理
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){
        return categoryService.list(page,pageSize);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Category category){
        return categoryService.update(request,category);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long ids){
        return categoryService.delete(ids);
    }

    /**
     * 获取分类列表(菜品或套餐)
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List> listType(@RequestParam(value = "type",required = false) Integer type){
        return categoryService.listType(type);
    }
}
