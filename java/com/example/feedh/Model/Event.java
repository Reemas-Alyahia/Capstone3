package com.example.feedh.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

// Nawaf - Event Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Event Name cannot be empty")
    @Size(min = 2, max = 50, message = "Event Name must be between 2 and 50 characters")
    private String name;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @NotEmpty(message = "Event Description cannot be empty")
    @Size(min = 1, max = 100, message = "Event Description must be between 1 and 100 characters")
    private String description;

    /// URL
    @Column(columnDefinition = "VARCHAR(100) NOT NULL")
    @NotEmpty(message = "Event Location cannot be empty")
    @Size(min = 1, max = 100, message = "Event Location must be between 1 and 100 characters")
    private String location;

    @Column(columnDefinition = "DATETIME NOT NULL")
    @NotNull(message = "Event Start Date Time cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @Column(columnDefinition = "DATETIME NOT NULL")
    @NotNull(message = "Event End Date Time cannot be empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL")
    @NotEmpty(message = "Event Status cannot be empty")
    @Pattern(regexp = "^(Scheduled|Ongoing|Completed)$",
            message = "Event Status must be either 'Scheduled', 'Ongoing', or 'Completed'")
    private String status;

    // Relations
    @ManyToOne
    @JsonIgnore
    private Admin admin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    @JsonIgnore
    private Set<EventParticipant> eventParticipants;
}