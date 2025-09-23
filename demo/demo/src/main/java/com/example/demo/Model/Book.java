package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity // 1
@Table(name = "books")
public class Book {

    @Id // 2
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3
    private Integer id;

    @NotBlank(message = "Title is mandatory") // 4
    private String title;

    private String description;

    @Positive(message = "Price must be a positive number") // 5
    private Double price;

    public Book() {
        // Constructor rỗng mặc định cần cho JPA
    }

    // Constructor có tham số để tạo đối tượng
    public Book(String title, String description, Double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}