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

    @GetMapping("/{id}")
    public ResponseEntity<Userdto> getUserById(@PathVariable Long id) {
        Userdto userdto = userservice.getUserById(id);
        if(userdto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userdto);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody Userdto userdto) {

            Userdto createdUser = userservice.createUser(userdto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email , @Valid @RequestBody Userdto userdto) {
            Userdto updateUser = userservice.updateUser(email, userdto);
            return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Userdto> deleteUser(@PathVariable Long id) {
        userservice.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserBasicDto>> getEmployeesByCompanyName(@RequestParam String companyName) {
        List<UserBasicDto> employees = userservice.getAllUserByCompanyName(companyName);
       if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
       }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/count-by-company")
    public Long countUsers(
            @RequestParam("companyName") String companyName,
            // Giới tính là tham số tùy chọn
            @RequestParam(value = "gender", required = false) String gender) {
        // Logic nghiệp vụ và việc ném exception được giữ nguyên trong Service
        // Nếu userservice ném ra IllegalArgumentException,
        // GlobalExceptionHandler sẽ tự động bắt và trả về 400 Bad Request

        return userservice.countUsersByCompanyAndGender(companyName , gender);
    }

        @GetMapping("/count-by-company-age")
        public String countUsersByAge(
                @RequestParam ("companyName") String companyName ,
                @RequestParam("ageLimit") int ageLimit){
            // 1. Gọi Service để lấy kết quả đếm
            long count = userservice.countUsersByCompanyAndAgeGreaterThan(companyName, ageLimit);
            // 2. Định dạng thông báo (hoặc có thể trả về một đối tượng DTO)
            String message = String.format("Số lượng nhân viên trên %d tuổi của công ty '%s' là: %d ",
                    ageLimit , companyName , count );
            // 3. Trả về String. Spring Boot tự động wrap thành 200 OK.
            // Nếu userservice ném ra IllegalArgumentException,
            // GlobalExceptionHandler sẽ tự động bắt và trả về 400 Bad Request.
            return message;
        }

    @GetMapping("/sum-staff-mark")
    public String sumStaffMark (
            @RequestParam("companyName") String companyName ,
            @RequestParam(value = "gender" , required = false ) String gender) {
        // 1. Gọi Service. Nếu Service ném ra IllegalArgumentException,
        // GlobalExceptionHandler sẽ tự động bắt và trả về 400 Bad Request.
        Long sumMark = userservice.sumStaffMarkByCompanyAndGender(companyName , gender);
        // 2. Xử lý giá trị null và định dạng chuỗi
        long safeSum = (sumMark != null) ? sumMark : 0;
        String filter ;
        if(gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("ALL")) {
            filter = "giới tính " + gender.toUpperCase();
        } else {
            filter = "tổng số ";
        }
        // 3. Định dạng thông báo trả về (Spring Boot tự wrap thành 200 OK)
        String message = String.format("Tổng điểm đánh giá nhân viên  %s trong công ty '%s' là: %d",
                filter , companyName , safeSum );
        return message ;

    }

    @PutMapping("/update-mark")
    public String updateUserMark(@RequestParam String email , @RequestParam Integer validate_staff_mark) {
        // 1. Gọi Service để thực hiện nghiệp vụ cập nhật

        userservice.updateStaffMark(email, validate_staff_mark);
        // 2. Trả về thông báo thành công. Spring Boot tự động wrap thành 200 OK.
        // Nếu userservice ném ra IllegalArgumentException (ví dụ: email không tồn tại),
        // Global Exception Handler sẽ tự động bắt lỗi và trả về 400 Bad Request.

        return "Cập nhập điểm thành công ";
    }
}
