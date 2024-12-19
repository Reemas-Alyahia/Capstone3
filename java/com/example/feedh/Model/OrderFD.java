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

// Reemas - OrderFD Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderFD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @NotNull(message = "Order Date cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime orderDateTime=LocalDateTime.now();

    @Column(columnDefinition = "INT NOT NULL")
    @NotNull(message = "Order Quantity cannot be empty")
    @Positive(message = "Order Quantity must be positive number")
    private Integer quantity;


    @Column(columnDefinition = "DECIMAL NOT NULL")
    @NotNull(message = "Order Total Amount cannot be empty")
    @Positive(message = "Order Total amount must be positive number")
    private Double totalAmount;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT ('Completed')")
    @NotEmpty(message = "Order Status cannot be empty")
    @Size(min = 1, max = 50, message = "Order Status must be between 1 and 50 characters")
    @Pattern(regexp = "^(Completed|Canceled)")
    private String status = "Completed";

    // Relations
    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "orderFD")
    private Set<Product> products;
}
