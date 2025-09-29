package com.example.demo.controller;

import com.example.demo.dto.UserBasicDto;
import com.example.demo.dto.Userdto;
import com.example.demo.service.Userservice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/user"})
public class UserController {

    @Autowired
    private Userservice userservice;

    @GetMapping
    public ResponseEntity<List<Userdto>> getAllUser() {
        return ResponseEntity.ok(userservice.getAllUsers());
    }

    @GetMapping({"/id"})
    public ResponseEntity<Userdto> getUserById(@PathVariable Long id) {
        Userdto userdto = userservice.getUserById(id);
        if(userdto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userdto);
    }

    @PostMapping({"/add"})
    public ResponseEntity<?> createUser(@Valid @RequestBody Userdto userdto) {
       try{
            Userdto createdUser = userservice.createUser(userdto);
                return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping({"/add1"})
    public ResponseEntity<?> createUser1(@Valid @RequestBody Userdto userdto) {
        Userdto createdUser = userservice.createUser(userdto);
        return ResponseEntity.ok(createdUser);
    }



    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email , @Valid @RequestBody Userdto userdto) {
        try {
            Userdto updateUser = userservice.updateUser(email, userdto);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } catch (IllegalArgumentException e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping({"/id"})
    public ResponseEntity<Userdto> deleteUser(@PathVariable Long id) {
        userservice.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserBasicDto>> getEmployeesByCompanyName(@RequestParam String companyName) {
        List<UserBasicDto> employees = userservice.getAllUserByCompanyName(companyName);
      ///  if (employees.isEmpty()) {
      ///      return ResponseEntity.noContent().build();
     ///   }
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/count-employees")
    public ResponseEntity<Long> countEmployees(@RequestParam String companyName , @RequestParam String gender) {
        try {
            long count = userservice.countUserByCompanyAndGender(companyName, gender);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/count-by-age")
    public Long countByAgeGreaterThan(@RequestParam String companyName , @RequestParam  int age) {
        return userservice.countUserByCompanyAndAge(companyName, age);
    }
}
