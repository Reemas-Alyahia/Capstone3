package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.AdminDTOout;
import com.example.feedh.DTOout.CustomerDTOout;
import com.example.feedh.DTOout.EventDTOout;
import com.example.feedh.DTOout.EventParticipantDTOout;
import com.example.feedh.Model.Admin;
import com.example.feedh.Model.Event;
import com.example.feedh.Model.EventParticipant;
import com.example.feedh.Repository.AdminRepository;
import com.example.feedh.Repository.EventParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Nawaf - Admin Service
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final EventParticipantRepository eventParticipantRepository;

    // CRUD - Start
    public List<AdminDTOout> getAllAdmin() {
        List<Admin> admins = adminRepository.findAll();
        List<AdminDTOout> adminDTOS = new ArrayList<>();

        for (Admin a : admins) {
            List<EventDTOout> eventDTOS = new ArrayList<>();
            for (Event e : a.getEvents()) {
                eventDTOS.add(new EventDTOout(e.getName(), e.getDescription(), e.getLocation(), e.getStartDateTime(), e.getEndDateTime(), e.getStatus()));
            }
            adminDTOS.add(new AdminDTOout(a.getName(), a.getEmail(), a.getPhoneNumber(), eventDTOS));
        }
        return adminDTOS;
    }

    public void addAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public void updateAdmin(Integer adminId, Admin admin) {
        Admin oldAdmin = adminRepository.findAdminById(adminId);
        if (oldAdmin == null) {
            throw new ApiException("Admin with ID: " + adminId + " was not found");
        }
        oldAdmin.setName(admin.getName());
        oldAdmin.setEmail(admin.getEmail());
        oldAdmin.setPhoneNumber(admin.getPhoneNumber());
        oldAdmin.setPassword(admin.getPassword());
        adminRepository.save(oldAdmin);
    }

    public void deleteAdmin(Integer adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("Admin with ID: " + adminId + " was not found");
        }
        adminRepository.delete(admin);
    }
    // CRUD - End

    // Services
    public void approveParticipation(Integer eventParticipantId, Integer adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("Admin not found");
        }
        EventParticipant eventParticipant = eventParticipantRepository.findEventParticipantById(eventParticipantId);
        if (eventParticipant == null) {
            throw new ApiException("Event Participant not found");
        }
        eventParticipant.setStatus("Accepted");
        eventParticipantRepository.save(eventParticipant);
    }

    public void rejectParticipation(Integer eventId, Integer adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("Admin not found");
        }
        EventParticipant eventParticipant = eventParticipantRepository.findEventParticipantById(eventId);
        if (eventParticipant == null) {
            throw new ApiException("Event Participant not found");
        }
        eventParticipant.setStatus("Rejected");
        eventParticipantRepository.save(eventParticipant);
    }

    public List<EventParticipantDTOout> getParticipantsByStatus(Integer adminId, String status) {
        List<EventParticipant> participants = eventParticipantRepository.findEventParticipantByStatus(status);
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("You don't have the permission to succeed");
        }

        List<EventParticipantDTOout> participantDTOS = new ArrayList<>();

        for (EventParticipant participant : participants) {

            CustomerDTOout customerDTOout = new CustomerDTOout(
                    participant.getCustomer().getName(),
                    participant.getCustomer().getEmail(),
                    participant.getCustomer().getPhoneNumber(),
                    participant.getCustomer().getAddress(),
                    participant.getCustomer().getRegisterStatus(),
                    null, // Assuming farms aren't needed for this response
                    null  // Assuming rentals aren't needed for this response
            );


            EventDTOout eventDTOout = new EventDTOout(
                    participant.getEvent().getName(),
                    participant.getEvent().getDescription(),
                    participant.getEvent().getLocation(),
                    participant.getEvent().getStartDateTime(),
                    participant.getEvent().getEndDateTime(),
                    participant.getEvent().getStatus()
            );

            // Add to the DTO list
            participantDTOS.add(new EventParticipantDTOout(
                    participant.getStatus(),
                    customerDTOout,
                    eventDTOout
            ));
        }
        return participantDTOS;
    }
}
