package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// Reemas - Customer DTO Out
@Data
@AllArgsConstructor
public class CustomerDTOout {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String registerStatus;
    private List<FarmDTOout> farms;
    private List<RentalDTOout> rentals;
}