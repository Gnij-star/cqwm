package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements MealService {
    private final SetmealMapper setmealMapper;

    @Override
    public PageResult<SetmealDTO> pageQuery(SetmealPageQueryDTO dto){
        Page<Setmeal> page = new Page<>(dto.getPage(),dto.getPageSize());
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        Page<Setmeal> list = setmealMapper.selectPage(page,wrapper);
        List<SetmealDTO> dtoList = list.getRecords().stream().map(item->{
            SetmealDTO meal = new SetmealDTO();
            BeanUtils.copyProperties(item,meal);
            return meal;
        }).collect(Collectors.toList());
        return new PageResult<>(list.getTotal(),dtoList);
    }
}
