package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setmeal")
@Slf4j
@Api(tags = "套餐管理")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping("/page")
    public Result<PageResult<SetmealVO>> page(SetmealPageQueryDTO query){
        PageResult<SetmealVO> pageResult =mealService.pageQuery(query);
        return Result.success(pageResult);
    }

//    // 删除数据接口
//    export const deleteSetmeal = (ids: string) => {
//        return request({
//                url: '/setmeal',
//                method: 'delete',
//                params: { ids }
//  })
//    }
//
//// 修改数据接口
//    export const editSetmeal = (params: any) => {
//        return request({
//                url: '/setmeal',
//                method: 'put',
//                data: { ...params }
//  })
//    }
    @PutMapping
    public Result<SetmealVO> update(@RequestBody SetmealDTO dto){
        SetmealVO meal = mealService.updateData(dto);
        return Result.success(meal);
    }

//// 新增数据接口
//    export const addSetmeal = (params: any) => {
//        return request({
//                url: '/setmeal',
//                method: 'post',
//                data: { ...params }
//  })
//    }
        @PostMapping
        public Result<SetmealVO> add(@RequestBody SetmealDTO setmealDTO){
            SetmealVO vo = mealService.add(setmealDTO);
            return Result.success(vo);
        }

        @GetMapping("/{id}")
        public Result<SetmealVO> detail(@PathVariable Long id){
            SetmealVO vo = mealService.detail(id);
            return Result.success(vo);
        }
}
