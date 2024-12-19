package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

// Ebtehal - Supplier DTO Out
@Data
@AllArgsConstructor
public class SupplierDTOout {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private List<ProductDTOout> productDTOS;
    private List<HeavyEquipmentDTOout> heavyEquipmentDTOS;
}
