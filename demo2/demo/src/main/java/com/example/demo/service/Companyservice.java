package com.example.demo.service;

import com.example.demo.dto.Companydto;
import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Companyservice {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Companydto> getAllCompany(){
        return companyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public Companydto getCompanyById(Long id){
        return companyRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    public Company createCompany(Companydto companydto) {
        Optional<Company> existingCompany = companyRepository.findByCompanyName(    companydto.getCompanyName());
        if(existingCompany.isPresent()){
            throw new IllegalArgumentException("Company name already exists");
        }
        Company company = convertToEntity(companydto);
        return companyRepository.save(company);
    }

    public Companydto updateCompany(Long id, Companydto companydto) {
        return companyRepository.findById(id).map(company -> {
            company.setCompanyName(companydto.getCompanyName());
            company.setDescription(companydto.getDescription());
            company.setWebsite(companydto.getWebsite());
            company.setAddress(companydto.getAddress());
            company.setPhone(companydto.getPhone());
            return convertToDto(companyRepository.save(company));
        }).orElse(null);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public Companydto convertToDto(Company company) {
        Companydto dto = new Companydto();
        dto.setCompanyName(company.getCompanyName());
        dto.setDescription(company.getDescription());
        dto.setAddress(company.getAddress());
        dto.setPhone(company.getPhone());
        dto.setWebsite(company.getWebsite());
        return dto;
    }

    public Company convertToEntity(Companydto dto) {
        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setDescription(dto.getDescription());
        company.setAddress(dto.getAddress());
        company.setPhone(dto.getPhone());
        company.setWebsite(dto.getWebsite());
        return company;
    }
}
