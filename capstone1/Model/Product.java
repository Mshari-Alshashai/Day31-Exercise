package com.example.capstone1.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, message = "The name should have more than 3 characters")
    @NotEmpty(message = "The name should not be empty")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "The price should not be null")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "The category ID should not be empty")
    @Column(nullable = false)
    private Integer categoryId;
}
