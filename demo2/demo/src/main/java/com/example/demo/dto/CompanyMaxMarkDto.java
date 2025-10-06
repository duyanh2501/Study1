package com.example.demo.dto;

import lombok.*;

@Data
@Getter
@Setter
public class CompanyMaxMarkDto {
    private String company ;
    private Integer maxMark ;

    public CompanyMaxMarkDto(String company, Integer maxMark) {
        this.company = company;
        this.maxMark = maxMark;
    }
}
