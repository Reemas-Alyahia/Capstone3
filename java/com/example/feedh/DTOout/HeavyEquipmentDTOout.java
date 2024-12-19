package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

// Ebtehal - HeavyEquipment DTO Out
@Data
@AllArgsConstructor
public class HeavyEquipmentDTOout {
    private String name;
    private Double pricePerHour;
    private Double insurance;
    private String status;
}
