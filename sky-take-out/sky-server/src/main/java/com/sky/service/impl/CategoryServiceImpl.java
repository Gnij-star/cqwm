package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.converter.CategoryConverter;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryConverter converter;

    @Override
    public PageResult selectPage(CategoryPageQueryDTO categoryPageQueryDTO){
            // 查询条件：分类名称/分类类型（菜品分类/套餐分类）
        String categoryName = categoryPageQueryDTO.getName();
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasText(categoryName)){
            wrapper.like(Category::getName,categoryName);
        }
        if(categoryPageQueryDTO.getType() != null){
            wrapper.eq(Category::getType,categoryPageQueryDTO.getType());
        }
        wrapper.orderByAsc(Category::getSort);
        Integer page = categoryPageQueryDTO.getPage();
        Integer pageSize = categoryPageQueryDTO.getPageSize();
        Page<Category> pageResult = new Page<>(page,pageSize);
        Page<Category> result = categoryMapper.selectPage(pageResult,wrapper);
        return new PageResult(result.getTotal(),result.getRecords());
    };


    @Override
    @Transactional
    public Result startOrStop(Integer status, Long id){
        if(status == null){
            throw  new BaseException("参数不能为空");
        }
        if(id == null){
            throw  new BaseException("id不能为空");
        }
        Category item = categoryMapper.selectById(id);
        if(item == null){
            throw new BaseException("分类不存在");
        }
        if(item.getStatus().equals(status)){
            return Result.success();
        }
        item.setStatus(status);
        categoryMapper.updateById(item);
        return Result.success();
    }


    @Override
    @Transactional
    public void delById(Long id){
        Category item = categoryMapper.selectById(id);
        if(item == null){
            throw new BaseException("分类不存在");
        }
        int cols = categoryMapper.deleteById(id);
        if(cols == 0){
            throw new BaseException("删除失败");
        }
    }

    @Override
    public CategoryDTO detail(Long id){
        Category category = categoryMapper.selectById(id);
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category,dto);
        return dto;
    }

    @Override
    @Transactional
    public CategoryDTO add(CategoryDTO categoryDTO){
//        Category item = new Category();
//        BeanUtils.copyProperties(categoryDTO,item);
        Category item = converter.toEntity(categoryDTO);
        int cols = categoryMapper.insert(item);
        if(cols == 0){
            throw new BaseException("新增失败");
        }
//        CategoryDTO dto = new CategoryDTO();
//        BeanUtils.copyProperties(item,dto);
        CategoryDTO dto = converter.toCategoryDTO(item);
        return dto;
    }


    @Override
    @Transactional
    public CategoryDTO update(Long id,CategoryDTO categoryDTO){
        Category isExist = categoryMapper.selectById(id);
        if(isExist == null){
            throw new BaseException("分类不存在");
        }
        Category item = converter.toEntity(categoryDTO);
        item.setId(id);
        int rows = categoryMapper.updateById(item);
        if(rows == 0){
            throw new BaseException("更新失败");
        }
        Category result = categoryMapper.selectById(id);
        CategoryDTO dto = converter.toCategoryDTO(result);
        return dto;
    }


    @Override
    public List<CategoryDTO> listByType(int type){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType,type).eq(Category::getStatus,1).orderByAsc(Category::getId).orderByAsc(Category::getSort);
        List<Category> list = categoryMapper.selectList(wrapper);
        return list.stream().map(category->{
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(category,dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
