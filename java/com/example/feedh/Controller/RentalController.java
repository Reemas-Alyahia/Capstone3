package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Rental;
import com.example.feedh.Service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

// Reemas - Rental Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rental")
public class RentalController {
    private final RentalService rentalService;
    // CRUD - Start
/// reemas
    @GetMapping("/get")
    public ResponseEntity getAllRentals() {
        return ResponseEntity.status(200).body(rentalService.getAllRentals());
    }
    /// reemas & Nawaf
    @PostMapping("/add/{customerId}/{farmer_id}/{heavyEquipment_id}")
    public ResponseEntity addRental(@PathVariable Integer customerId, @PathVariable Integer farmer_id, @PathVariable Integer heavyEquipment_id, @RequestBody @Valid Rental rental) {
        rentalService.addRental(customerId,farmer_id,heavyEquipment_id, rental);
        return ResponseEntity.status(200).body(new ApiResponse("Rental has been added to customer with ID: " + customerId + " successfully"));
    }
    /// reemas
    @PutMapping("/update/{rentalId}")
    public ResponseEntity updateRental(@PathVariable Integer rentalId, @RequestBody @Valid Rental rental) {
        rentalService.updateRental(rentalId, rental);
        return ResponseEntity.status(200).body(new ApiResponse("Rental with ID: " + rentalId + " has been updated successfully"));
    }
    // CRUD - End

    // Services
    // Reemas - Endpoint used to calculate the total price of renting a heavy equipment by hour
    @PostMapping("/calculate-rental-price/{rentalStartDate}/{rentalEndDate}/{price}")
    public ResponseEntity calculateRentalPrice(@PathVariable LocalDateTime rentalStartDate,@PathVariable LocalDateTime rentalEndDate,@PathVariable Double price){
        Double rentalPrice = rentalService.calculateRentalPrice(rentalStartDate, rentalEndDate,price );
        return ResponseEntity.status(200).body(rentalPrice);
    }

    // Reemas - Endpoint runs every 10 minutes to check and update all rentals status
    @PutMapping("/update-rental-Status")
    public ResponseEntity updateRentalStatus(){
        rentalService.updateRentalStatus();
        return ResponseEntity.status(200).body(new ApiResponse("Rental status method is running"));

    }
    // Reemas - Endpoint returns list of rentals by price and status
    @GetMapping("/get-ByPrice-Status/{rentalId}/{price}/{status}")
    public  ResponseEntity getRentalByPriceAndStatus(@PathVariable Integer rentalId, @PathVariable Double price,@PathVariable String status){
        return  ResponseEntity.status(200).body(rentalService.getRentalByPriceAndStatus(rentalId, price, status));
    }
}
