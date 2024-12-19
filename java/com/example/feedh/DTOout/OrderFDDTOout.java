package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// Reemas - OrderFD DTO Out
@Data
@AllArgsConstructor
public class OrderFDDTOout {
    private LocalDateTime orderDateTime;
    private Integer quantity;
    private Double totalAmount;
    private String status;
    private List<ProductDTOout> productDTOS;
}
