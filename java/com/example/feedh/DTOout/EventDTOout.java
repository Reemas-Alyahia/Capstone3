package com.example.feedh.DTOout;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// Nawaf - Event DTO Out
@Data
@AllArgsConstructor
public class EventDTOout {
    private String name;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}
