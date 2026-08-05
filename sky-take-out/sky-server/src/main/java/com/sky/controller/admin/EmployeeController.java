package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/api/employee")
@Slf4j
@Api(tags = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    @ApiOperation("新增员工")
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return  Result.success();
    }

    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    public Result<PageResult> query(EmployeePageQueryDTO employeePageQueryDTO){
        //参数校验
        if(employeePageQueryDTO.getPage() <= 0 ){
            employeePageQueryDTO.setPage(1);
        }
        if(employeePageQueryDTO.getPageSize() <= 0){
            employeePageQueryDTO.setPageSize(10);
        }
        PageResult pageResult = employeeService.query(employeePageQueryDTO);
        return Result.success(pageResult);
    }


    @ApiOperation("启用/禁用员工")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    @ApiOperation("查询员工详情")
    @GetMapping("/detail/{id}")
    public Result<EmployeeDTO> getById(@PathVariable Long id){
        EmployeeDTO result = employeeService.getById(id);
        return Result.success(result);
    }


    @ApiOperation("更新详情")
    @PutMapping("/{id}")
    public Result updateById(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO){
        EmployeeDTO result = employeeService.update(id,employeeDTO);
        return Result.success(result);
    }

}
