package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface CategoryService extends IService<Category> {
    PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO);

    Result startOrStop(Integer status,Long id);
}
