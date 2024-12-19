package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

// Reemas - Rental Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rental {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  @NotNull(message = "Rental Date Start cannot be empty")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDateTime=LocalDateTime.now();

  @Column(columnDefinition = "DATETIME NOT NULL")
  @NotNull(message = "Rental End Date cannot be empty")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDateTime;

  @Column(columnDefinition = "double")
  //@Positive(message = "Rental Price must be positive number")
  private Double price;

  @Column(columnDefinition = "VARCHAR(50) DEFAULT ('Active')")
  @Size(min = 1, max = 50, message = "Rental Status must be between 1 and 50 characters")
  @Pattern(regexp = "^(Active|Cancelled|Completed)$",
          message = "Rental Status must be either 'Active', 'Cancelled, or 'Completed'")
  private String status = "Active";

  // Relations
  @ManyToOne
  @JsonIgnore
  private Customer customer;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "rental")
  private Set<HeavyEquipment> heavyEquipments;
}
