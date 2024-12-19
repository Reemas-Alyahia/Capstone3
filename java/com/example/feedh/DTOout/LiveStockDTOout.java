package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

// Ebtehal - LiveStock DTO Out
@Data
@AllArgsConstructor
public class LiveStockDTOout {
    private String type;
    private String breed;
    private Integer quantity;
    private String feedType;
}
