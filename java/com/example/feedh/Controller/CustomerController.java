package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Customer;
import com.example.feedh.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Reemas - Customer Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    // CRUD - Start
    // Reemas
    @GetMapping("/get")
    public ResponseEntity getAllCustomers() {
        return ResponseEntity.status(200).body(customerService.getAllCustomers());
    }
    //Reemas
    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody @Valid Customer customer) {
        customerService.addCustomer(customer);
        return ResponseEntity.status(200).body(new ApiResponse("Customer has been added successfully"));
    }
     //Reemas
    @PutMapping("/update/{customerId}")
    public ResponseEntity updateCustomer(@PathVariable Integer customerId, @RequestBody @Valid Customer customer) {
        customerService.updateCustomer(customerId, customer);
        return ResponseEntity.status(200).body(new ApiResponse("Customer with ID: " + customerId + " has been updated successfully"));
    }
     //Reemas
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customer with ID: " + customerId + " has been deleted successfully"));
    }
    // CRUD - End
}
