package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u INNER JOIN u.company c WHERE c.companyName = :companyName")
    List<User> findByCompanyWithInnerJoin(@Param("companyName") String companyName);

    @Query("SELECT COUNT(u) FROM User u JOIN u.company c WHERE c.companyName = :companyName AND u.gender = :gender")
    long countUsersByCompanyNameAndGender(@Param("companyName") String companyName, @Param("gender") String gender);

    // đếm TỔNG (bỏ qua giới tính)
    @Query("SELECT COUNT(u) FROM User u JOIN u.company c WHERE c.companyName = :companyName")
    long countTotalUsersByCompanyName(@Param("companyName") String companyName);

    @Query("SELECT COUNT(u) " +
            "FROM User u JOIN u.company c " +
            "WHERE c.companyName = :companyName " +
            "  AND u.birthday < :dateThreshold")
    long countUsersByCompanyNameAndAgeGreaterThan(
            @Param("companyName") String companyName,
            @Param("dateThreshold") Date dateThreshold);

    @Query("SELECT SUM(u.validateStaffMark) " +
            "FROM User u JOIN u.company c " +
            "WHERE c.companyName = :companyName " +
            "  AND u.gender = :gender")
    Long sumStaffMarkByCompanyNameAndGender (
            @Param("companyName") String companyName ,
            @Param("gender" ) String gender);

    @Query("SELECT SUM(u.validateStaffMark) " +
            "FROM User u JOIN u.company c " +
            "WHERE c.companyName = :companyName")
    Long sumTotalStaffMarkByCompanyName(
            @Param("companyName") String companyName);

}


