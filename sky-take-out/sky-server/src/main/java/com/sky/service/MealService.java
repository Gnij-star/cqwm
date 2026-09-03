package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

public interface MealService extends IService<Setmeal> {
    PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO dto);
    SetmealVO add(SetmealDTO dto);
}
