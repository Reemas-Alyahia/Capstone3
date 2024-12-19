package com.example.feedh.Repository;

import com.example.feedh.Model.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Nawaf - EventParticipant Repository
@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Integer> {
    EventParticipant findEventParticipantById(Integer id);
    List<EventParticipant> findEventParticipantByStatus(String status);
}
