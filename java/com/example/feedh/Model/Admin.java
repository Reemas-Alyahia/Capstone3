package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Nawaf - Admin Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Admin Name cannot be empty")
    @Size(min = 2, max = 50, message = "Admin Name must be between 2 and 50 characters")
    private String name;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL UNIQUE")
    @NotEmpty(message = "Admin Email cannot be empty")
    @Size(min = 8, max = 50, message = "Admin Email must be between 8 and 50 characters")
    @Email(message = "Admin Email must be in valid email format")
    private String email;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL UNIQUE")
    @NotEmpty(message = "Admin Phone Number cannot be empty")
    @Pattern(regexp = "^05\\d{8}$",
            message = "Admin Phone Number must start with '05' and be exactly 10 digits long.")
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Admin Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$",
            message = "Customer Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least 8 characters long.")
    private String password;

    // Relations
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "admin")
   @JsonIgnore
   private Set<Event> events;
}
