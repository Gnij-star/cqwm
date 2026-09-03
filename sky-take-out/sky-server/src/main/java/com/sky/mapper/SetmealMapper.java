package com.sky.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    Page<SetmealVO> pageQueryWithCategory(Page<SetmealVO> page, SetmealPageQueryDTO dto);

}
