package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Companydto {

    private Long id;

    @NotBlank(message = "Company name is required")
    @Length(max = 20 , message = "Company name must be at most 20 characters")
    private String companyName;

    @NotBlank(message = "description is required")
    @Length(max = 20 , message = "Description must be at most 20 characters")
    private String description;

    @Length(max = 200 , message = "Website must be at most 200 characters")
    private String website;

    @NotBlank(message = "Address is required")
    @Length(max = 20 , message = "Address must be at most 20 characters")
    private String address;

    @NotBlank(message = "Phone is required")
    @Length(max = 10 , message = "Phone must be at most 20 characters")
    private String phone;


}
