package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BaseException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
//        Employee employee = employeeMapper.getByUsername(username);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);
        Employee employee = employeeMapper.selectOne(wrapper);

        log.info("employee=====> {}", employee);
        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        log.info("对比=====> {}，{}", employee.getPassword(),password);
        //密码比对
        // 明文和数据库中的密文进行比对，matches自动获取盐值再对比
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    public static void main(String[] args) {
//        // 临时生成加密后的密码。注意：这里直接 new 一个，不需要注入
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encoded = encoder.encode("123456");
//        System.out.println("加密后的密码：" + encoded);
    }


    public void save(EmployeeDTO employeeDTO){
        String username = employeeDTO.getUsername();
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);
        Employee isExistEmployee = employeeMapper.selectOne(wrapper);
        if(isExistEmployee != null){
            throw new BaseException(400,"用户名已存在");
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        employee.setPassword(encode);
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setCreateUser(BaseContext.getCurrentId());
        // employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult query(EmployeePageQueryDTO employeePageQueryDTO) {
        Page<Employee> page = new Page<>(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasText(employeePageQueryDTO.getName())){
            wrapper.like(Employee::getName,employeePageQueryDTO.getName())
                    .orderByDesc(Employee::getCreateTime);
        }
        employeeMapper.selectPage(page, wrapper);
        List<EmployeeVO> voList = page.getRecords().stream().map(item->{
            EmployeeVO vo = new EmployeeVO();
            BeanUtils.copyProperties(item,vo);
            return vo;
        }).collect(Collectors.toList());

        return new PageResult(page.getTotal(),voList);
    }

    @Override
    public void startOrStop(Integer status,Long id){
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getId,id);
        Employee employee = employeeMapper.selectOne(wrapper);
        if(employee != null){
            employee.setStatus(status);
            employeeMapper.updateById(employee);
        }
    }

    @Override
    public EmployeeDTO getById(Long id){
        Employee employee = employeeMapper.selectById(id);
        if(employee == null){
            throw new BaseException("用户不存在");
        }
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee,dto);
        return dto;
    }

    @Override
    public EmployeeDTO update(Long id,EmployeeDTO employeeDTO){
        Employee isExit = employeeMapper.selectById(id);
        if(isExit == null){
            throw new BaseException("用户不存在");
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setId(id);
        int rows = employeeMapper.updateById(employee);
        if(rows == 0){
            throw new BaseException("更新失败");
        }
        EmployeeDTO emp = new EmployeeDTO();
        BeanUtils.copyProperties(employee,emp);
        return emp;
    }
}
