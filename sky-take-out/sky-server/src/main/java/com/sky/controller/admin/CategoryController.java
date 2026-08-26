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

import javax.validation.Valid;
import java.util.List;

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
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    // 路径：/category?id=123
    @DeleteMapping
    public Result del(@RequestParam Long id){
        categoryService.delById(id);
        return Result.success();
    }

    // /detail?id=123
    @GetMapping("/detail")
    public Result<CategoryDTO> detail(@RequestParam Long id){
        CategoryDTO categoryDTO = categoryService.detail(id);
        return Result.success(categoryDTO);
    }

    // 商务套餐、13、套餐分类
    @PostMapping
    public Result add(@RequestBody CategoryDTO categoryDTO){
        categoryService.add(categoryDTO);
        return Result.success();
    }


    @PutMapping("/{id}")
    public Result<CategoryDTO> update(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO result = categoryService.update(id,categoryDTO);
        return Result.success(result);
    }

    @GetMapping("/list")
    public  Result<List<CategoryDTO>> list(@RequestParam(required = false)int type){
        List<CategoryDTO> list = categoryService.listByType(type);
        return Result.success(list);
    }
}
