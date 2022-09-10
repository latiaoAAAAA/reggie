package cn.edu.lingnan.service;

import cn.edu.lingnan.common.R;
import cn.edu.lingnan.entity.Category;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    R<Page> list(Integer page, Integer pageSize);

    R<String> update(Category category);

    R<String> delete(Long ids);

    R<List> listType(Integer type);
}
