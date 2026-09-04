package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.SetmealdishMapper;
import com.sky.result.PageResult;
import com.sky.service.MealService;
import com.sky.service.SetmealdishService;
import com.sky.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements MealService {
    private final SetmealMapper setmealMapper;
    private final CategoryMapper categoryMapper;
    private final SetmealdishMapper setmealdishMapper;
    private final SetmealdishService setmealdishService;

    @Override
    public PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO dto){
        // Page<Setmeal> page = new Page<>(dto.getPage(),dto.getPageSize());
        // LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        // Page<Setmeal> list = setmealMapper.selectPage(page,wrapper);
        // 改成自定义联表查询
        Page<SetmealVO> page = new Page<>(dto.getPage(),dto.getPageSize());
        Page<SetmealVO> list = setmealMapper.pageQueryWithCategory(page,dto);

        List<SetmealVO> dtoList = list.getRecords().stream().map(item->{
            SetmealVO meal = new SetmealVO();
            BeanUtils.copyProperties(item,meal);
            return meal;
        }).collect(Collectors.toList());
        return new PageResult<>(list.getTotal(),dtoList);
    }


    @Override
    @Transactional
    public SetmealVO add(SetmealDTO dto){
        Setmeal meal = new Setmeal();
        BeanUtils.copyProperties(dto,meal);
        if (dto.getCategoryId() != null && dto.getCategoryId() > 0){
            meal.setCategoryId(dto.getCategoryId());
        }else{
            throw new BaseException("分类id不能为空");
        }
           setmealMapper.insert(meal);

        List<SetmealDish> setmealDishes  = dto.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(dish->dish.setSetmealId(meal.getId()));
            //           批量插入菜品数据到关联表
            setmealdishService.saveBatch(setmealDishes);
        }
        return setmealMapper.getByIdWithDishes(meal.getId());
    }

    @Override
    @Transactional
    public SetmealVO updateData(SetmealDTO dto){
        if(dto.getId()==null){
            throw new BaseException("id不能为空");
        }
        Setmeal meal = new Setmeal();
        BeanUtils.copyProperties(dto,meal);
        int rows = setmealMapper.updateById(meal);
        if(rows==0){
            throw new BaseException("更新失败");
        }

        // 先删旧关联，再写入新菜品列表
        setmealdishService.remove(
                new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, dto.getId())
        );
        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(dish -> {
                dish.setId(null);
                dish.setSetmealId(dto.getId());
            });
            setmealdishService.saveBatch(setmealDishes);
        }

        return setmealMapper.getByIdWithDishes(dto.getId());
    }

    @Override
    public SetmealVO detail(Long id){
        if(id == null){
            throw new BaseException("id不能为空");
        }
        SetmealVO vo = setmealMapper.getByIdWithDishes(id);
        if(vo == null){
            throw new BaseException("套餐不存在");
        }
        return vo;
    }
}
