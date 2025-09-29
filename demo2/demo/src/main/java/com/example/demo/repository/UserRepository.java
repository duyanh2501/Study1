package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u INNER JOIN u.company c WHERE c.companyName = :companyName")
    List<User> findByCompanyWithInnerJoin(@Param("companyName") String companyName);

    @Query("SELECT COUNT(u) FROM User u INNER JOIN Company c ON u.company.id = c.id WHERE c.companyName = :companyName AND u.gender = :gender")
    long countUsersByCompanyAndGender(@Param("companyName") String companyName, @Param("gender") String gender);

    @Query("SELECT COUNT(u) FROM User u INNER JOIN Company c ON u.company.id = c.id WHERE c.companyName = :companyName")
    long countUsersByCompany(@Param("companyName") String companyName);

    @Query("SELECT COUNT(u) " +
            "FROM User u " +
            "JOIN u.company c " +
            "WHERE c.companyName = :companyName AND " +
            "(YEAR(CURRENT_DATE) - YEAR(u.birthday)) > :age")
    long countByCompanyNameAndAgeGreaterThan(@Param("companyName") String companyName, @Param("age") int age);


}
