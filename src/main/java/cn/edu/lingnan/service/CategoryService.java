package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Category;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CategoryService extends IService<Category> {
    R<String> save(HttpServletRequest request, Category category);

    R<Page> list(Integer page, Integer pageSize);

    R<String> update(HttpServletRequest request, Category category);

    R<String> delete(Long ids);

    R<List> listType(Integer type);
}
