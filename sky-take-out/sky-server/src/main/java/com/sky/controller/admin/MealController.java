package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MealService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setmeal")
@Slf4j
@Api(tags = "套餐管理")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping("/page")
    public Result<PageResult<SetmealDTO>> page(SetmealPageQueryDTO query){
        PageResult<SetmealDTO> pageResult =mealService.pageQuery(query);
        return Result.success(pageResult);
    }

}
