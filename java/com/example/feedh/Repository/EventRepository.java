package com.example.feedh.Repository;

import com.example.feedh.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Nawaf - EventParticipant Repository
@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findEventById(Integer id);

    //find event between start and end
    List<Event>findEventByStartDateTimeBetween(LocalDateTime start,LocalDateTime end);

    List<Event>findEventByStatusAndLocation(String status,String location);


    @Query("select e from Event e where e.location=?1 and e.startDateTime=?2 and e.endDateTime=?3")
    List<Event>getEventByLocationAndStartDateAndEndDateTime(String location, LocalDateTime startDate,LocalDateTime endDateTime);

}
