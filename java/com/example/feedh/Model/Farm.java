package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Nawaf - Farm Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL UNIQUE")
    @NotEmpty(message = "Farm Name cannot be empty")
    @Size(min = 2, max = 50, message = "Customer Name must be between 2 and 50 characters")
    private String name;

    // add URL for location
    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Farm Location cannot be empty")
    @Size(min = 2, max = 50, message = "Farm Location must be between 2 and 50 characters")
    private String location;

    @Column(columnDefinition = "DECIMAL NOT NULL")
    @NotNull(message = "Farm Size cannot be empty")
    @Positive(message = "Farm Size must be positive number")
    private Double size;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Farm Type cannot be empty")
    @Size(min = 1, max = 50, message = "Farm Type must be between 1 and 50 characters")
    @Pattern(regexp = "^(Agricultural|Livestock|Mixed)$",
            message = "Farm Type must be either 'Agricultural', 'Livestock', or 'Mixed'")
    private String type;

    // Relations
    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "farm")
    private Set<Farmer> farmers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "farm")
    private Set<Plant> plants;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "farm")
    private Set<LiveStock> liveStocks;
}