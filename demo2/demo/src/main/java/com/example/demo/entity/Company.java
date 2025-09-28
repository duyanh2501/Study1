package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "COMPANY")
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_name" , nullable = false , length = 20)
    private String companyName;

    @Column(name = "description" , nullable = false , length = 20)
    private String description;

    @Column(name = "website" , length = 200)
    private String website;

    @Column(name = "address" , nullable = false , length = 4000)
    private String address;

    @Column(name = "phone" , nullable = false , length = 20 )
    private String phone;

    @OneToMany(mappedBy = "company")
    private Set<User> users;

}
