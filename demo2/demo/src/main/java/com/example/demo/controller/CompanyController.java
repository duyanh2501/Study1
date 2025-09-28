package com.example.demo.controller;


import com.example.demo.dto.Companydto;
import com.example.demo.entity.Company;
import com.example.demo.service.Companyservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class    CompanyController {
    @Autowired
    private Companyservice companyservice;

    @GetMapping
    public ResponseEntity<List<Companydto>> getAllCompany() {
        return ResponseEntity.ok(companyservice.getAllCompany());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Companydto> getCompanyById(@PathVariable Long id) {
        Companydto company = companyservice.getCompanyById(id);
            if (company == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody Companydto companydto) {
        try {
            Company saveCompany = companyservice.createCompany(companydto);
            Companydto response = companyservice.convertToDto(saveCompany);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Companydto> updateCompany(@PathVariable Long id, @Valid @RequestBody Companydto companydto) {
        Companydto updatedCompany = companyservice.updateCompany(id, companydto);
        if (updatedCompany == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Companydto> deleteCompany(@PathVariable Long id) {
        companyservice.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

}
