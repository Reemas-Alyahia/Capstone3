package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Nawaf - Farmer Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Farmer Name cannot be empty")
    @Size(min = 2, max = 50, message = "Farmer Name must be between 2 and 50 characters")
    private String name;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL UNIQUE")
    @NotEmpty(message = "Farmer Phone Number cannot be empty")
    @Pattern(regexp = "^05\\d{8}$",
            message = "Farmer Phone Number must start with '05' and be exactly 10 digits long.")
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @NotEmpty(message = "Farmer Address cannot be empty")
    @Size(min = 1, max = 100, message = "Farmer Address must be between 1 and 100 characters")
    private String address;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Farmer Password cannot be empty")
    @Size(max = 50, message = "Customer Password must be 50 characters maximum")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$",
            message = "Customer Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.")
    private String password;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Farmer Visa Type cannot be empty")
    @Size(min = 1, max = 50, message = "Farmer Visa Type must be between 1 and 50 characters")
    @Pattern(regexp = "^(Farmer|Shepherd)$",
            message = "Farmer Visa type must be either 'Farmer' or 'Shepherd'")
    private String visaType;

    // Relations
    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private Farm farm;
}