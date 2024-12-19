package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Farm;
import com.example.feedh.Service.FarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Ebtehal - Farm Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/farm")
public class FarmController {
    private final FarmService farmService;
    // CRUD - Start
    @GetMapping("/get")
    public ResponseEntity getAllFarms() {
        return ResponseEntity.status(200).body(farmService.getAllFarms());
    }

    @PostMapping("/add/{ownerId}")
    public ResponseEntity addFarm(@PathVariable Integer ownerId, @RequestBody @Valid Farm farm) {
        farmService.addFarm(ownerId, farm);
        return ResponseEntity.status(200).body(new ApiResponse("Farm has been added successfully"));
    }

    @PutMapping("/update/{farmId}")
    public ResponseEntity updateFarm(@PathVariable Integer farmId, @RequestBody @Valid Farm farm) {
        farmService.updateFarm(farmId, farm);
        return ResponseEntity.status(200).body(new ApiResponse("Farm with ID: " + farmId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{ownerId}/{farmId}")
    public ResponseEntity deleteFarm(@PathVariable Integer ownerId,@PathVariable Integer farmId) {
        farmService.deleteFarm(ownerId, farmId);
        return ResponseEntity.status(200).body(new ApiResponse("Farm with ID: " + farmId + " has been deleted successfully"));
    }
    // CRUD - End

    // Services
    // Reemas - Endpoint returns a list of farms belongs to a customer in specific location
    @GetMapping("/get/by-customer/{customerId}/farm/{farmId}/location/{location}")
        public ResponseEntity getFarmByLocation(@PathVariable Integer customerId,@PathVariable Integer farmId ,@PathVariable String location){
            List<Farm>farms=farmService.getFarmByLocation(customerId, farmId, location);
            return ResponseEntity.status(200).body(farms);
    }

    // Nawaf - Endpoint returns a list of farms belongs to a customer
    @GetMapping("/get/by-customer/{customerId}")
    public ResponseEntity getFarmByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(farmService.getFarmByCustomer(customerId));
    }

    // Nawaf - Endpoint returns a list of farms belongs to a customer in specific type
    @GetMapping("/get/by-customer/{customerId}/type/{type}")
    public ResponseEntity getFarmByCustomerAndType(@PathVariable Integer customerId, @PathVariable String type) {
        return ResponseEntity.status(200).body(farmService.getFarmByCustomerAndType(customerId, type));
    }
}
