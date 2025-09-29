package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "\"users\"")
@Getter
@Setter
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name" , nullable = false , length = 20)
    private String username;

    @Column(name = "email" , nullable = false , length = 20 , unique = true)
    private String email;

    @Column(name = "password" , nullable = false , length = 200)
    private String password;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "address" , length = 4000)
    private String address;

    @Column(name = "gender" , length = 10)
    private String gender;

    @Column(name = "phone" , length = 10)
    private String phone;

    @Column(name = "validate_staff_mark" )
    private Integer validateStaffMark;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
