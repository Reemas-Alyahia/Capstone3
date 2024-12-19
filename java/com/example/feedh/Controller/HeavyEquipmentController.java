package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.HeavyEquipment;
import com.example.feedh.Service.HeavyEquipmentService;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Ebtehal - HeavyEquipment Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/heavy-equipment")
public class HeavyEquipmentController {
    private final HeavyEquipmentService heavyEquipmentService;
    // CRUD - Start
    @GetMapping("/get")
    public ResponseEntity getAllHeavyEquipments() {
        return ResponseEntity.status(200).body(heavyEquipmentService.getAllHeavyEquipments());
    }

    @PostMapping("/add/by/{supplierId}")
    public ResponseEntity addHeavyEquipment(@PathVariable Integer supplierId, @RequestBody @Valid HeavyEquipment heavyEquipment) {
        heavyEquipmentService.addHeavyEquipment(supplierId, heavyEquipment);
        return ResponseEntity.status(200).body(new ApiResponse("Heavy Equipment has been added to supplier with ID: " + supplierId + " successfully"));
    }

    @PutMapping("/update/{heavyEquipmentId}/by/{supplierId}")
    public ResponseEntity updateHeavyEquipment(@PathVariable Integer heavyEquipmentId, @PathVariable Integer supplierId, @RequestBody @Valid HeavyEquipment heavyEquipment) {
        heavyEquipmentService.updateHeavyEquipment(heavyEquipmentId, supplierId, heavyEquipment);
        return ResponseEntity.status(200).body(new ApiResponse("Heavy Equipment with ID: " + heavyEquipmentId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{heavyEquipmentId}/by/{supplierId}")
    public ResponseEntity deleteHeavyEquipment(@PathVariable Integer heavyEquipmentId, @PathVariable Integer supplierId) {
        heavyEquipmentService.deleteHeavyEquipment(heavyEquipmentId, supplierId);
        return ResponseEntity.status(200).body(new ApiResponse("Heavy Equipment with ID: " + heavyEquipmentId + " has been deleted successfully"));
    }
    // CRUD - End

    // Services
    // Reemas - Endpoint changes the heavy equipment status to Available
    @PutMapping("/chang-status/{heavyEquipmentId}/{rentalId}")
    public ResponseEntity changStatusForTheHeavyE(@PathVariable Integer heavyEquipmentId, @PathVariable Integer rentalId){
        heavyEquipmentService.changStatusForTheHeavyEquipment(heavyEquipmentId, rentalId);
        return ResponseEntity.status(200).body(new ApiResponse("Done Change the status for the Heavy Equipment"));
    }

    // Reemas - Endpoint returns a list of heavy equipments by status
    @GetMapping("/get/by-status/{supplier_id}/{status}")
    public ResponseEntity getHeavyEquipmentByStatus(@PathVariable Integer supplier_id,@PathVariable String status ){
        List<HeavyEquipment> heavyEquipments =heavyEquipmentService.getHeavyEquipmentByStatus(supplier_id, status);
        return ResponseEntity.status(200).body(heavyEquipments);
    }
}
