package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    private final DishMapper dishMapper;

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        Page<Dish> page = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(dishPageQueryDTO.getName())) {
            wrapper.like(Dish::getName, dishPageQueryDTO.getName());
        }
        if (dishPageQueryDTO.getCategoryId() != null) {
            wrapper.eq(Dish::getCategoryId, dishPageQueryDTO.getCategoryId());
        }
        if (dishPageQueryDTO.getStatus() != null) {
            wrapper.eq(Dish::getStatus, dishPageQueryDTO.getStatus());
        }
        wrapper.orderByDesc(Dish::getUpdateTime);
//        Page<Dish> result = dishMapper.selectPage(page, wrapper);
        Page<DishVO> result = dishMapper.selectPageWithCategory(page,dto);
        return new PageResult(result.getTotal(), result.getRecords());
    }
}
