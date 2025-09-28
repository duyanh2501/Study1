package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class Userdto {
    private Integer id;

    @NotBlank(message = "Username is required")
    @Length(max = 20 , message = " User name must be at most 20 characters")
    private String username;

    @NotBlank(message = "Email is requied")
    @Length(max = 20 , message = "Email name must be at most 20 characters")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is requied")
    @Length(max = 200 , message = "Password name must be at most 200 characters")
    private String password;
    private String repassword;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String address;

    @Pattern(regexp = "[UFM]", message = "Gender must be 'U', 'F', or 'M'")
    private String gender;

    private String phone;
    private Integer validateStaffMark;
    private Long companyId;


}
