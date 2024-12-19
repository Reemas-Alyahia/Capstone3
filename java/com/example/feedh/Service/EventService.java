package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.EventDTOout;
import com.example.feedh.Model.Admin;
import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Event;
import com.example.feedh.Repository.AdminRepository;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Nawaf - Event Service
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    // CRUD - Start
    public List<EventDTOout> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventDTOout> eventDTOS = new ArrayList<>();

        for (Event e : events) {
            eventDTOS.add(new EventDTOout(e.getName(), e.getDescription(), e.getLocation(), e.getStartDateTime(), e.getEndDateTime(), e.getStatus()));
        }
        return eventDTOS;
    }

    public void addEvent(Integer adminId, Event event) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("You don't have the permission to add a new event");
        }
        event.setAdmin(admin);
        eventRepository.save(event);
        notifyCustomersAboutEvent(event);
    }

    public void updateEvent(Integer eventId, Integer adminId, Event event) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("You don't have the permission to update an event");
        }
        Event oldEvent = eventRepository.findEventById(eventId);
        if (oldEvent == null) {
            throw new ApiException("Event with ID: " + eventId + " was not found");
        }
        oldEvent.setName(event.getName());
        oldEvent.setDescription(event.getDescription());
        oldEvent.setLocation(event.getLocation());
        oldEvent.setStartDateTime(event.getStartDateTime());
        oldEvent.setEndDateTime(event.getEndDateTime());
        oldEvent.setStatus(event.getStatus());
        eventRepository.save(oldEvent);
    }

    public void deleteEvent(Integer eventId, Integer adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("You don't have the permission to delete an event");
        }
        Event event = eventRepository.findEventById(eventId);
        if (event == null) {
            throw new ApiException("Event with ID: " + eventId + " was not found");
        }
        eventRepository.delete(event);
    }
    // CRUD - End

    // Services
    public List<Event> getEventByDate(LocalDateTime start, LocalDateTime end){
        if (start.isAfter(end)){
            throw new ApiException("start date must be before end date");
        }
        return eventRepository.findEventByStartDateTimeBetween(start, end);

    }

   //// Event By Location And Date
    public List<Event>getEventByLocationAndDate(Integer customer_id,String location, LocalDateTime startDate, LocalDateTime endDateTime){
        Customer customer=customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customer_id + " was not found");
        }
        List<Event>eventList=eventRepository.getEventByLocationAndStartDateAndEndDateTime(location, startDate, endDateTime);

        if(eventList.isEmpty()){
            throw new ApiException("Sorry look like there’s no event like this details");
        }
        return eventList;
    }

    public List<Event>findEventByStatusAndLocation(String status,String location ){
       List<Event>eventList=eventRepository.findEventByStatusAndLocation(status, location) ;
       if(eventList.isEmpty()){
           throw  new ApiException("Sorry look like there’s no event like this details");
       }
       return  eventList;
    }

    // Nawaf - Notification method used to notify all customers when an admin creates a new event
    private void notifyCustomersAboutEvent(Event event) {
        List<Customer> customers = customerRepository.findAll();

        String subject = "New Event Added: " + event.getName();
        String body = String.format(
                "Dear Customer,\n\n" +
                        "We are excited to announce a new event: '%s'.\n\n" +
                        "Event Details:\n" +
                        "- Description: %s\n" +
                        "- Location: %s\n" +
                        "- Start Date: %s\n" +
                        "- End Date: %s\n\n" +
                        "We look forward to your participation. For more details, please contact us.\n\n" +
                        "Best regards,\n" +
                        "Your Farm Management Team",
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getStartDateTime().toString(),
                event.getEndDateTime().toString()
        );

        for (Customer customer : customers) {
            if (customer.getEmail() != null) {
                emailService.sendEmail(customer.getEmail(), subject, body);
            }
        }
    }
}
