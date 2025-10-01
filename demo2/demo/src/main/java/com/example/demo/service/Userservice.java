package com.example.demo.service;

import com.example.demo.dto.UserBasicDto;
import com.example.demo.dto.Userdto;
import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Userservice {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<Userdto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Userdto getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    // 1. Validate email uniqueness
    public Userdto createUser(Userdto userdto) {
        Optional<User> exexistingUser = userRepository.findByEmail(userdto.getEmail());
        if (exexistingUser.isPresent()) {
            throw new IllegalArgumentException("Email exist, please use other");
        }
        // 2. Validate password and repassword
        if (!userdto.getPassword().equals(userdto.getRepassword())) {
            throw new IllegalArgumentException("password and repassword not the same");
        }

        User user = convertToEntity(userdto);
        user.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));

        Company company = companyRepository.findById(userdto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found "));
        user.setCompany(company);


        User savedUser = userRepository.save(user);
            return convertToDto(savedUser);

        }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Userdto updateUser(String email, Userdto userdto) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
        User usertoUpdate = existingUser.get();

        if (!email.equals(userdto.getEmail())) {
            throw new IllegalArgumentException("Email does not match");
        }

        if (!userdto.getPassword().equals(userdto.getRepassword())) {
            throw new IllegalArgumentException("password and repassword not the same");
        }

        usertoUpdate.setPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));

        usertoUpdate.setUsername(userdto.getUsername());
        usertoUpdate.setBirthday(userdto.getBirthday());
        usertoUpdate.setAddress(userdto.getAddress());
        usertoUpdate.setGender(userdto.getGender());
        usertoUpdate.setPhone(userdto.getPhone());
        usertoUpdate.setValidateStaffMark(userdto.getValidateStaffMark());
        userRepository.save(usertoUpdate);

        return convertToDto(usertoUpdate);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private Userdto convertToDto(User user) {
        Userdto dto = new Userdto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setBirthday(user.getBirthday());
        dto.setGender(user.getGender());
        dto.setValidateStaffMark(user.getValidateStaffMark());
        if (user.getCompany() != null) {
            dto.setCompanyId(user.getCompany().getId());
        }
        return dto;
    }

    private User convertToEntity(Userdto dto) {
        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setBirthday(dto.getBirthday());
        entity.setGender(dto.getGender());
        entity.setValidateStaffMark(dto.getValidateStaffMark());
        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
            entity.setCompany(company);
        }
        return entity;
    }


    public List<UserBasicDto> getAllUserByCompanyName(String companyName) {
        List<User> users = userRepository.findByCompanyWithInnerJoin(companyName);
        return users.stream().map(this::convertToBasicDto).collect(Collectors.toList());
    }

    private UserBasicDto convertToBasicDto(User user) {
        UserBasicDto Basicdto = new UserBasicDto();
        Basicdto.setUserName(user.getUsername());
        Basicdto.setEmail(user.getEmail());
        Basicdto.setBirthday(user.getBirthday());
        Basicdto.setAddress(user.getAddress());
        Basicdto.setGender(user.getGender());
        Basicdto.setPhone(user.getPhone());
        return Basicdto;
    }

    public long countUsersByCompanyAndGender(String companyName, String genderString) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên công ty không được để trống.");
        }

        // 1. Kiểm tra Công ty có tồn tại không
        if (companyRepository.findByCompanyName(companyName).isEmpty()) {
            throw new IllegalArgumentException("Lỗi: Không tìm thấy công ty với tên: " + companyName);
        }

        // 2. Trường hợp Đếm Tổng (genderString là null, rỗng hoặc "ALL")
        if (genderString == null || genderString.trim().isEmpty() || genderString.equalsIgnoreCase("ALL")) {
            return userRepository.countTotalUsersByCompanyName(companyName);
        }

        // Chuẩn hóa và kiểm tra giá trị giới tính
        String normalizedGender = genderString.toUpperCase();
        if (!List.of("U", "F", "M").contains(normalizedGender)) {
            throw new IllegalArgumentException("Giá trị giới tính không hợp lệ. Chỉ chấp nhận: M, F, U, hoặc ALL/NULL.");
        }

        // 3. Trường hợp Đếm theo Giới tính cụ thể
        return userRepository.countUsersByCompanyNameAndGender(companyName, normalizedGender);
    }

    public long countUsersByCompanyAndAgeGreaterThan(String companyName, int ageLimit) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten cong ty khong de trong");
        }
        if (ageLimit < 0) {
            throw new IllegalArgumentException("Tuoi gioi han > 0 ");
        }
        if(companyRepository.findByCompanyName(companyName).isEmpty()){
            throw new IllegalArgumentException("Error : Company not found");
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -ageLimit);
        Date dateThreshold = cal.getTime();

        return userRepository.countUsersByCompanyNameAndAgeGreaterThan(companyName, dateThreshold);
    }

    public Long sumStaffMarkByCompanyAndGender(String companyName, String genderString) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten cong ty khong de trong");
        }
        if (companyRepository.findByCompanyName(companyName).isEmpty()) {
            throw new IllegalArgumentException("Error : Company not found");
        }
        if (genderString == null || genderString.trim().isEmpty() || genderString.equalsIgnoreCase("ALL")) {
            return userRepository.sumTotalStaffMarkByCompanyName(companyName);
        }
        String normalizedGender = genderString.toUpperCase();
        if (!List.of("U", "F", "M").contains(normalizedGender)) {
            throw new IllegalArgumentException("Giá trị giới tính không hợp lệ. Chỉ chấp nhận: M, F, U, hoặc ALL/NULL.");
        }
        return userRepository.sumStaffMarkByCompanyNameAndGender(companyName, normalizedGender);

    }

}
