package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

// Reemas - Product DTO Out
@Data
@AllArgsConstructor
public class ProductDTOout {
    private String name;
    private String category;
    private String description;
    private Double price;
    private Integer quantity;

    public ProductDTOout(){

    }
}
