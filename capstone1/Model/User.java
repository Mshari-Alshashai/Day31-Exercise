package com.example.capstone1.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Check(constraints = "role = admin or role = customer")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 6, message = "The username should have more than 5 characters")
    @NotEmpty(message = "The username should not be empty")
    @Column(unique = true, nullable = false)
    private String username;

    @NotEmpty(message = "The password should not be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{7,}$",message = "Password should have letters and numbers and be more than 6 characters")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "The email should not be empty")
    @Email(message = "Wrong format for the email")
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "The role should not be empty")
    @Pattern(regexp = "admin|customer",message = "The role should be Admin or customer")
    @Column(nullable = false)
    private String role;


    @NotNull(message = "The balance should not be null")
    @Positive(message = "The balance should be positive")
    @Column(nullable = false)
    private Double balance;

//    private ArrayList<String> purchasedProducts=new ArrayList<>();
//
//    private ArrayList<Product> wishlist=new ArrayList<>();

}
