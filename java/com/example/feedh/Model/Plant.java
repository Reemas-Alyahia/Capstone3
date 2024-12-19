package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Ebtehal - Plant Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Plant Type cannot be empty")
    @Size(min = 1, max = 50, message = "Plant Type must be between 1 and 50 characters")
    @Pattern(regexp = "^(Palm|Fruits|Vegetables)$",
            message = "Plant Type must be either 'Palm', 'Fruits', or 'Vegetables'")
    private String type;

    @Column(columnDefinition = "INT NOT NULL")
    @NotNull(message = "Plant Quantity cannot be empty")
    @Positive(message = "Plant Quantity must be positive number")
    private Integer quantity;

    // Relations
    @ManyToOne
    @JsonIgnore
    private Farm farm;
}
