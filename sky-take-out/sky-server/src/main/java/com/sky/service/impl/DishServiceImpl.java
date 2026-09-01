package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.converter.DishConverter;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    private final DishMapper dishMapper;
    private final DishConverter dishConverter;

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> page = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
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
        Page<DishVO> result = dishMapper.selectPageWithCategory(page,dishPageQueryDTO);
        return new PageResult(result.getTotal(), result.getRecords());
    }


    @Override
    public DishDTO detail(Long id){
        Dish dish = dishMapper.selectById(id);
        if(dish == null){
            throw new BaseException("id不存在");
        }
        DishDTO dto = new DishDTO();
        BeanUtils.copyProperties(dish,dto);
        return dto;
    }


    @Override
    public DishDTO updateDish(DishDTO dto){
        Dish item = dishMapper.selectById(dto.getId());
        if(item == null){
            throw new BaseException("菜品不存在");
        }
//        Dish dish = new Dish();
//        BeanUtils.copyProperties(dto,dish);
//        dishMapper.updateById(dish);
//        DishDTO dishDTO = new DishDTO();
//        BeanUtils.copyProperties(dish,dishDTO);
        Dish dish = dishConverter.toEntity(dto);
        dishMapper.updateById(dish);
        Dish updateDish=dishMapper.selectById(dto.getId());
        return dishConverter.toDishDTO(updateDish);
    }


    @Override
    public void delBatch(List<Long> ids){
        if(ids == null || ids.isEmpty()){
            throw new BaseException("请选择要删除的菜品");
        }
        dishMapper.deleteBatchIds(ids);
    }


    @Override
    public DishDTO addDish(DishDTO dto){
        Dish dish = dishConverter.toEntity(dto);
        dishMapper.insert(dish);
        Dish newDish = dishMapper.selectById(dish.getId());
        return dishConverter.toDishDTO(newDish);
    }
}
