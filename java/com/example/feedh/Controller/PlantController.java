package com.example.feedh.Controller;
import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.Model.Plant;
import com.example.feedh.Service.PlantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Ebtehal - Plant Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plant")
public class PlantController {
    private final PlantService plantService;
    // CRUD - Start
    @GetMapping("/get")
    public ResponseEntity getAllPlants() {
        return ResponseEntity.status(200).body(plantService.getAllPlants());
    }

    @PostMapping("/add/{farmId}")
    public ResponseEntity addFarm(@PathVariable Integer farmId, @RequestBody @Valid Plant plant) {
        plantService.addPlant(farmId, plant);
        return ResponseEntity.status(200).body(new ApiResponse("Plant has been added to farm with ID: " + farmId + " successfully"));
    }

    @PutMapping("/update/{plantId}")
    public ResponseEntity updatePlant(@PathVariable Integer plantId, @RequestBody @Valid Plant plant) {
        plantService.updatePlant(plantId, plant);
        return ResponseEntity.status(200).body(new ApiResponse("Plant with ID: " + plantId + " has been updated successfully"));
    }

    @DeleteMapping("/delete/{farmId}/{plantId}")
    public ResponseEntity deletePlant(@PathVariable Integer farmId,@PathVariable Integer plantId){
        plantService.deletePlant(farmId, plantId);
        return ResponseEntity.status(200).body(new ApiResponse("Plant with ID: " + plantId + " has been updated successfully"));
    }
    // CRUD - End

    // Ebtehal - Endpoint lets the customer to transfer their plants between customer's farms
    @PutMapping("/transfer/{farm1}/{farm2}/{ownerId}/{type}/{quantityToTransfer}")
    public ResponseEntity transferPlant(
            @PathVariable Integer farm1,
            @PathVariable Integer farm2,
            @PathVariable Integer ownerId,
            @PathVariable String  type,
            @PathVariable Integer quantityToTransfer) {
        plantService.transferPlantBetweenFarms(farm1, farm2, ownerId, type, quantityToTransfer);
        return ResponseEntity.status(200)
                .body(new ApiResponse("Successfully transferred " + quantityToTransfer + " plants from farm " + farm1 + " to farm " + farm2));
    }
}