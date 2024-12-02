package com.example.capstone1.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MerchantStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "The name should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer productID;

    @NotNull(message = "The ID should not be empty")
    @Column(columnDefinition = "int not null")
    private Integer merchantID;

    @Min(value = 10,message = "The stock have to be more than 10")
    @Positive(message = "The stock should not be 0 or negative")
    @Column(columnDefinition = "int not null")
    private Integer stock;
}
