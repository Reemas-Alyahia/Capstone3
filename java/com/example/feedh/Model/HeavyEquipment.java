package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Ebtehal - HeavyEquipment Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HeavyEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Heavy Equipment Name cannot be empty")
    @Size(min = 2, max = 50, message = "Heavy Equipment Name must be between 2 and 50 characters")
    private String name;

    @Column(columnDefinition = "DECIMAL NOT NULL")
    @NotNull(message = "Heavy Equipment Price cannot be empty")
    @Positive(message = "Heavy Equipment Price must be positive number")
    private Double pricePerHour;

    @Column(columnDefinition = "DECIMAL NOT NULL")
    @NotNull(message = "Heavy Equipment Insurance cannot be empty")
    @Positive(message = "Heavy Equipment Insurance must be positive number")
    private Double insurance;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT ('Active')")
    @NotEmpty(message = "Heavy Equipment status cannot be empty")
    @Size(min = 1, max = 50, message = "Heavy Equipment Status must be between 1 and 50 characters")
    @Pattern(regexp = "^(Available|Rented)$",
            message = "Heavy Equipment Status must be either 'Available' or 'Rented'")
    private String status = "Active";

    // Relations
    @ManyToOne
    @JsonIgnore
    private Rental rental;

    @ManyToOne
    @JsonIgnore
    private Supplier supplier;
}

