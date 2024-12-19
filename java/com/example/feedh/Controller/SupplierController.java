package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.DTOout.DTOoutSUP;
import com.example.feedh.Model.Supplier;
import com.example.feedh.Service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Ebtehal - Supplier Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplier")
public class SupplierController {
    private final SupplierService supplierService;
    // CRUD - Start
    @GetMapping("/get")
    public ResponseEntity getAllSuppliers() {
        return ResponseEntity.status(200).body(supplierService.getAllSuppliers());
    }

    @PostMapping("/add")
    public ResponseEntity addSupplier(@RequestBody @Valid Supplier supplier) {
        supplierService.addSupplier(supplier);
        return ResponseEntity.status(200).body(new ApiResponse("Supplier has been added successfully"));
    }

    @PutMapping("/update/{supplierId}")
    public ResponseEntity updateSupplier(@PathVariable Integer supplierId, @RequestBody @Valid Supplier supplier) {
        supplierService.updateSupplier(supplierId, supplier);
        return ResponseEntity.status(200).body(new ApiResponse("Supplier with ID: " + supplierId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity deleteSupplier(@PathVariable Integer supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.status(200).body(new ApiResponse("Supplier with ID: " + supplierId + " has been deleted successfully"));
    }
    // CRUD - End

    // Ebtehal - Endpoint sends emails to all rental customers whose their rentals expires soon (3 days or less)
    @GetMapping("/get/email")
    public ResponseEntity getRentalsNearExpiration() {
        return ResponseEntity.status(200).body(supplierService.getRentalsNearExpiration());
    }
    //Ebtehal
    @GetMapping("/supplier-address/{address}")
    public ResponseEntity getSupplierId(@PathVariable String address){
        return ResponseEntity.status(200).body(supplierService.getSupplierByAddress(address));
    }
   //Ebtehal
   @GetMapping("/by-product-price/{price}")
   public ResponseEntity getSuppliersByProductPrice(@PathVariable Double price) {
       List<DTOoutSUP> suppliers = supplierService.getSuppliersByProductPrice(price);
       return ResponseEntity.status(200).body(suppliers);
   }


    //Ebtehal
    @GetMapping("/by-heavy-equipment-price/{price}")
    public ResponseEntity getSuppliersByHeavyEquipmentRentPrice(@PathVariable Double price) {
        List<DTOoutSUP> suppliers = supplierService.getSuppliersByHeavyEquipmentRentPrice(price);
        return ResponseEntity.status(200).body(suppliers);
    }

}
