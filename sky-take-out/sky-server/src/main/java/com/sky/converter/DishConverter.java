package com.sky.converter;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishConverter {
    Dish toEntity(DishDTO dishDTO);
    DishDTO toDishDTO(Dish dish);

}
