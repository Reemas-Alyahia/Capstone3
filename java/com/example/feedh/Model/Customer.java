package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Reemas - Customer Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Customer Name cannot be empty")
    @Size(min = 2, max = 50, message = "Customer Name must be between 2 and 50 characters")
    private String name;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL UNIQUE")
    @NotEmpty(message = "Customer Email cannot be empty")
    @Size(min = 8, max = 50, message = "Customer Email must be between 8 and 50 characters")
    @Email(message = "Customer Email must be in valid email format")
    private String email;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL UNIQUE")
    @NotEmpty(message = "Customer Phone Number cannot be empty")
    @Pattern(regexp = "^05\\d{8}$",
            message = "Customer Phone number must start with '05' and be exactly 10 digits long.")
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @NotEmpty(message = "Customer Address cannot be empty")
    @Size(min = 1, max = 100, message = "Customer Address must be between 1 and 100 characters")
    private String address;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Customer Password cannot be empty")
    @Size(max = 50, message = "Customer Password must be 50 characters maximum")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$",
            message = "Customer Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.")
    private String password;

    @Column(columnDefinition = "BOOLEAN NOT NULL")
    @NotNull(message = "Customer Foundation File cannot be empty")
    private Boolean foundationFile;

    @Column(columnDefinition = "INT NOT NULL")
    @NotNull(message = "Customer Agricultural Register cannot be empty")
    @Pattern(regexp = "^\\d{6}$", message = "Customer Agricultural Register must be exactly 6 digits")
    private String agriculturalRegister;

    @Column(columnDefinition = "varchar(30) not null")
    @NotEmpty(message = "Customer Agricultural Register Status cannot be empty")
    @Pattern(regexp = "^(Active|Expired)$",
            message = "Customer Agricultural Register Status must be either 'Active' or 'Expired'")
    private String registerStatus;

    // Relations
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Farm> farms;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<OrderFD> orders;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Rental> rentals;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<EventParticipant> eventParticipants;
}
