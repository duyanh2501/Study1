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

    @GetMapping("/count-by-company")
    public ResponseEntity<?> countUsers(
            @RequestParam("companyName") String companyName,
            // Giới tính là tham số tùy chọn
            @RequestParam(value = "gender", required = false) String gender) {

        try {
            long count = userservice.countUsersByCompanyAndGender(companyName, gender);

            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count-by-company-age")
    public ResponseEntity<?> countUsersByAge(
            @RequestParam ("companyName") String companyName ,
            @RequestParam("ageLimit") int ageLimit){
        try {
            long count = userservice.countUsersByCompanyAndAgeGreaterThan(companyName, ageLimit);
            String message = String.format("Số lượng nhân viên trên %d tuổi của công ty '%s' là: %d",
                    ageLimit, companyName, count);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sum-staff-mark")
    public ResponseEntity<?> sumStaffMark (
            @RequestParam("companyName") String companyName ,
            @RequestParam(value = "gender" , required = false ) String gender) {
                try {
                    Long sumMark = userservice.sumStaffMarkByCompanyAndGender(companyName, gender);
                    long safeSum = (sumMark != null) ? sumMark : 0;
                    String filter = (gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("ALL")) ?
                            "giới tính " + gender.toUpperCase() : " tổng số " ;
                    String message = String.format("Tổng điểm đánh giá của nhân viên có %s trong công ty '%s' là: %d",
                            filter, companyName, safeSum);
                    return ResponseEntity.ok(sumMark);
                }catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
    }

}
