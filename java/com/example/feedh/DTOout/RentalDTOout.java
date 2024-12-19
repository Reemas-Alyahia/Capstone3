package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// Reemas - Rental DTO Out
@Data
@AllArgsConstructor
public class RentalDTOout {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Double price;
    private String status;
}
