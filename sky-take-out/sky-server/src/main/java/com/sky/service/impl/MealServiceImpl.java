package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.MealService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements MealService {
    private final SetmealMapper setmealMapper;
    private final CategoryMapper categoryMapper;

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


    @Override
    public SetmealVO add(SetmealDTO dto){
        Setmeal meal = new Setmeal();
        BeanUtils.copyProperties(dto,meal);
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0){
            meal.setCategoryId(dto.getCategoryId());
        }else{
            throw new BaseException("分类id不能为空");
        }
           setmealMapper.insert(meal);
           Setmeal vo = setmealMapper.selectById(meal.getId());
           SetmealVO result = new SetmealVO();
           BeanUtils.copyProperties(vo,result);
           Category category = categoryMapper.selectById(vo.getCategoryId());
           if(category != null){
            result.setCategoryName(category.getName());
           }
           return result;
    }
}
