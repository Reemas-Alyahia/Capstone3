package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Ebtehal - LiveStock Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LiveStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Live Stock Type cannot be empty")
    @Size(min = 1, max = 50, message = "Live Stock Type must be between 1 and 50 characters")
    @Pattern(regexp = "^(Sheep|Camel)$",
            message = "Live Stock Type must be either 'Sheep' or 'Camel'")
    private String type;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Live Stock Breed cannot be empty")
    @Size(min = 1, max = 50, message = "Live Stock Breed must be between 1 and 50 characters")
    private String breed;

    @Column(columnDefinition = "INT NOT NULL")
    @NotNull(message = "Live Stock Quantity cannot be empty")
    @Positive(message = "Live Stock Quantity must be positive number")
    private Integer quantity;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Live Stock Feed Type cannot be empty")
    @Size(min = 1, max = 50, message = "Live Stock Feed Type must be between 1 and 50 characters")
    @Pattern(regexp = "^(Grain|Hay|Grass|Mixed)$",
            message = "Live Stock Feed Type must be either 'Grain', 'Hay', 'Grass', or 'Mixed'")
    private String feedType;

    // Relations
    @ManyToOne
    @JsonIgnore
    private Farm farm;
}
