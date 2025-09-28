package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

@Data

public class UserBasicDto {
    private String userName;
    private String email;
    private Date birthday;
    private String address;
    private String gender;
    private String phone;
}
