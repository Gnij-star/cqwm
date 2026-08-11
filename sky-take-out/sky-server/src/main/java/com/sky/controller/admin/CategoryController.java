package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.exception.BaseException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@Slf4j
@Api("分类管理")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/page")
    public Result<PageResult> query(CategoryPageQueryDTO categoryPageQueryDTO){
        Integer page = categoryPageQueryDTO.getPage();
        Integer pageSize = categoryPageQueryDTO.getPageSize();
        if(page <= 0){
            categoryPageQueryDTO.setPage(1);
        }
        if(pageSize <= 0){
            categoryPageQueryDTO.setPageSize(10);
        }
        PageResult pageResult = categoryService.selectPage(categoryPageQueryDTO);
        return Result.success(pageResult);

    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }
}
