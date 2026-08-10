package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;
    @Override
    public PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO){
            // 查询条件：分类名称/分类类型（菜品分类/套餐分类）
        String categoryName = categoryPageQueryDTO.getName();
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasText(categoryName)){
            wrapper.like(Category::getName,categoryName);
        }
        Integer page = categoryPageQueryDTO.getPage();
        Integer pageSize = categoryPageQueryDTO.getPageSize();
        Page<Category> pageResult = new Page<>(page,pageSize);
        Page<Category> result = categoryMapper.selectPage(pageResult,wrapper);
        return new PageResult(result.getTotal(),result.getRecords());
    };

}
