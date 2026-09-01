package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;

public interface MealService extends IService<Setmeal> {
    PageResult<SetmealDTO> pageQuery(SetmealPageQueryDTO dto);
}
