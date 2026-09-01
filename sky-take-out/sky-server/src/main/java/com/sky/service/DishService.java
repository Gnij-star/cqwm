package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService extends IService<Dish> {

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    DishDTO detail(Long id);

    DishDTO updateDish(DishDTO dto);

    void delBatch(List<Long> ids);

    DishDTO addDish(DishDTO dto);

    void updateStatus(Integer status,Long id);
}
