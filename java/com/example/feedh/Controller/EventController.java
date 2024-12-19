package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Event;
import com.example.feedh.Service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Nawaf - Event Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {
  private final EventService eventService;
  @GetMapping("/get")
  public ResponseEntity getAllEvents(){
      return ResponseEntity.status(200).body(eventService.getAllEvents());
  }

  @PostMapping("/add/by/{adminId}")
  public ResponseEntity addEvent(@PathVariable Integer adminId, @RequestBody @Valid Event event){
    eventService.addEvent(adminId,event);
    return ResponseEntity.status(200).body(new ApiResponse("Event has been added successfully"));
  }

  @PutMapping("/update/{eventId}/by/{adminId}")
  public ResponseEntity updateEvent(@PathVariable Integer eventId, @PathVariable Integer adminId, @RequestBody @Valid Event event){
    eventService.updateEvent(eventId, adminId, event);
    return ResponseEntity.status(200).body(new ApiResponse("Event with ID: " + eventId + " has been updated successfully"));
  }

  @DeleteMapping("/delete/{eventId}/by/{adminId}")
  public ResponseEntity deleteEvent(@PathVariable Integer eventId, @PathVariable Integer adminId){
    eventService.deleteEvent(eventId, adminId);
    return ResponseEntity.status(200).body(new ApiResponse("Event with ID: " + eventId + " has been deleted successfully"));
  }
  // CRUD - End

  // Services
  // Ebthal - Endpoint returns a list of events between two dates
  @GetMapping("/date/start/{start}/end/{end}")
  public ResponseEntity getDate(@PathVariable LocalDateTime start, @PathVariable LocalDateTime end){
    List<Event> events = eventService.getEventByDate(start, end);
    return ResponseEntity.status(200).body(events);
  }

  // Reemas - Endpoint returns a list of events by a location in specific date
  @GetMapping("/get/by-location-and-date")
  public ResponseEntity getEventByLocationAndDate(@RequestBody Integer customer_id, @RequestBody String location, @RequestBody LocalDateTime startDate
        ,@RequestBody LocalDateTime endDateTime){
    List<Event>events = eventService.getEventByLocationAndDate(customer_id, location, startDate, endDateTime);
    return ResponseEntity.status(200).body(events);
  }

  // Reemas - Endpoint returns a list of events by status in specific location
  @GetMapping("/get/status/{status}/location/{location}")
  public  ResponseEntity findEventByStatusAndLocation(@PathVariable String status, @PathVariable String location){
    return  ResponseEntity.status(200).body(eventService.findEventByStatusAndLocation(status, location));
  }
}
