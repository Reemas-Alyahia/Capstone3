package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Farmer;
import com.example.feedh.Service.FarmerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Nawaf - Farmer Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/farmer")
public class FarmerController {
    private final FarmerService farmerService;
    // CRUD - Start
    @GetMapping("/get")
    public ResponseEntity getAllFarmers() {
        return ResponseEntity.status(200).body(farmerService.getAllFarmers());
    }

    @PostMapping("/add/in/{farmId}/by/{customerId}")
    public ResponseEntity addFarmer(@PathVariable Integer farmId, @PathVariable Integer customerId, @RequestBody @Valid Farmer farmer) {
        farmerService.addFarmer(farmId, customerId, farmer);
        return ResponseEntity.status(200).body(new ApiResponse("Farmer has been added successfully"));
    }

    @PutMapping("/update/{farmerId}/by/{customerId}")
    public ResponseEntity updateFarmer(@PathVariable Integer farmerId, @PathVariable Integer customerId, @RequestBody @Valid Farmer farmer) {
        farmerService.updateFarmer(farmerId, customerId, farmer);
        return ResponseEntity.status(200).body(new ApiResponse("Farmer with ID: " + farmerId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{farmerId}/by/{customerId}")
    public ResponseEntity deleteFarmer(@PathVariable Integer farmerId, @PathVariable Integer customerId) {
        farmerService.deleteFarmer(farmerId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Farmer with ID: " + farmerId + " has been deleted successfully"));
    }
    // CRUD - End

    // Services
    // Ebtehal - Endpoint lets the customer to transfer their farmers between customer's farms
    @PutMapping("/transfer-farmer/{farmerId}/{farm1Id}/{farm2Id}/{customerId}")
    public ResponseEntity transferFarmer(
            @PathVariable Integer farmerId,
            @PathVariable Integer farm1Id,
            @PathVariable Integer farm2Id,
            @PathVariable Integer customerId) {
        farmerService.transferFarmer(farmerId, farm1Id, farm2Id, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Farmer " + farmerId + " successfully transferred from farm " + farm1Id + " to farm " + farm2Id));
    }

    // Nawaf - Endpoint returns a list of farmers registered on a specific customer
    @GetMapping("/get/by-customer/{customerId}")
    public ResponseEntity getFarmerByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(farmerService.getFarmerByCustomer(customerId));
    }

    // Nawaf - Endpoint returns a list of farmers registered on a specific customer, in a specific farm
    @GetMapping("/get/by-customer/{customerId}/farm/{farmId}")
    public ResponseEntity getFarmerByCustomerAndFarm(@PathVariable Integer customerId, @PathVariable Integer farmId) {
        return ResponseEntity.status(200).body(farmerService.getFarmerByCustomerAndFarm(customerId, farmId));
    }
    // Nawaf - Endpoint returns a list of farmers registered on a specific customer, with a specific visa type
    @GetMapping("/get/by-customer/{customerId}/visa-type/{visaType}")
    public ResponseEntity getFarmerByCustomerAndVisaType(@PathVariable Integer customerId, @PathVariable String visaType) {
        return ResponseEntity.status(200).body(farmerService.getFarmerByCustomerAndVisaType(customerId, visaType));
    }
}