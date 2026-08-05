package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private String sex;

    @NotBlank
    private String idNumber;

}
