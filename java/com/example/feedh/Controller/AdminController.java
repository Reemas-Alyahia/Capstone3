package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Admin;
import com.example.feedh.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Nawaf - Admin Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
private final AdminService adminService;
    // CRUD - Start
    @GetMapping("/get")
    public ResponseEntity getAllAdmins(){
        return ResponseEntity.status(200).body(adminService.getAllAdmin());
    }

    @PostMapping("/add")
    public ResponseEntity addAdmin(@RequestBody @Valid Admin admin) {
        adminService.addAdmin(admin);
        return ResponseEntity.status(200).body(new ApiResponse("Admin has been added successfully"));
    }

    @PutMapping("/update/{adminId}")
    public ResponseEntity updateAdmin(@PathVariable Integer adminId, @RequestBody @Valid Admin admin) {
        adminService.updateAdmin(adminId, admin);
        return ResponseEntity.status(200).body(new ApiResponse("Admin with ID: " + adminId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity deleteAdmin(@PathVariable Integer adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(200).body(new ApiResponse("Admin with ID: " + adminId + " has been deleted successfully"));
    }
    // CRUD - End

    //Services
    // Ebtehal - Endpoint lets the admin to accept an event participant
    @PutMapping("/approve/{eventParticipantId}/{adminId}")
    public ResponseEntity approveParticipation(@PathVariable Integer eventParticipantId, @PathVariable Integer adminId) {
        adminService.approveParticipation(eventParticipantId, adminId);
        return ResponseEntity.status(200).body(new ApiResponse("Participation approved successfully"));
    }

    // Ebtehal - Endpoint lets the admin to reject an event participant
    @PutMapping("/reject/{eventParticipantId}/{adminId}")
    public ResponseEntity rejectParticipation(@PathVariable Integer eventParticipantId, @PathVariable Integer adminId) {
        adminService.rejectParticipation(eventParticipantId, adminId);
        return ResponseEntity.status(200).body(new ApiResponse("Participation rejected successfully"));
    }
    
    // Nawaf - Reemas: Endpoint returns a list of event participants by their status to the admin
    @GetMapping("/get/by-status/{adminId}/{status}")
    public ResponseEntity getParticipantsByStatus(@PathVariable Integer adminId, @PathVariable String status) {
        return ResponseEntity.status(200).body(adminService.getParticipantsByStatus(adminId, status));
    }
}
