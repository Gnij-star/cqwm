package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.ApiModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dish")
@ApiModel("菜品管理")
@Slf4j
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        if(dishPageQueryDTO.getPage() <= 0){
            dishPageQueryDTO.setPage(1);
        }
        if(dishPageQueryDTO.getPageSize() <= 0){
            dishPageQueryDTO.setPageSize(10);
        }
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

}
