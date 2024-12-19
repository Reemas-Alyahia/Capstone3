package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Nawaf - EventParticipant Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT ('Pending')")
    @Size(min = 1, max = 50, message = "Event Participant Status must be between 1 and 50 characters")
    @Pattern(regexp = "^(Pending|Accepted|Rejected)$",
            message = "Event Participant Status must be either 'Pending', 'Accepted', or 'Rejected'")
    private String status = "Pending";

    // Relations
    @ManyToOne
    @JsonIgnore
    private Event event;

    @ManyToOne
    @JsonIgnore
    private Customer customer;
}

