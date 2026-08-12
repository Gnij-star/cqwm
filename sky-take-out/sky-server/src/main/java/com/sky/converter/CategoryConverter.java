package com.sky.converter;

import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryConverter {
    Category toEntity(CategoryDTO categoryDTO);
    CategoryDTO toCategoryDTO(Category category);
}
