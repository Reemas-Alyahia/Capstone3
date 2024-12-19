package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

// Ebtehal - SupplierDTO Out (Special)
@Data
@AllArgsConstructor
///  that supplier DTO is for compare between supplier by there price
public class DTOoutSUP {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
