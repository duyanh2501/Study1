package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CompanyTotalMarkDto {
    private String company;
    private Long totalMark;

    public CompanyTotalMarkDto(String company, Long totalMark) {
        this.company = company;
        this.totalMark = totalMark;
    }

}
