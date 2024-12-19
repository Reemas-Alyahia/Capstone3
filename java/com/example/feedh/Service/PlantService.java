package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.PlantDTOout;
import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Farm;
import com.example.feedh.Model.Plant;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.FarmRepository;
import com.example.feedh.Repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Ebtehal - Plant Service
@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final FarmRepository farmRepository;
    private final CustomerRepository customerRepository;

    // CRUD - Start
    public List<PlantDTOout> getAllPlants() {
        List<Plant> plants = plantRepository.findAll();
        List<PlantDTOout> plantDTOS = new ArrayList<>();

        for (Plant plant : plants) {
            plantDTOS.add(new PlantDTOout(plant.getType(), plant.getQuantity()));
        }
        return plantDTOS;
    }

    //Add new plant
    public void addPlant(Integer farmId, Plant plant) {
        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }
        plant.setFarm(farm);
        plantRepository.save(plant);
    }

    //update plant
    public void updatePlant(Integer plantId, Plant plant) {
        Plant oldPlant = plantRepository.findPlantById(plantId);
        if (oldPlant == null) {
            throw new ApiException("Plant with ID: " + plantId + " was not found");
        }
        oldPlant.setType(plant.getType());
        oldPlant.setQuantity(plant.getQuantity());
//        oldPlant.setFarm(plant.getFarm());
        plantRepository.save(oldPlant);
    }

    //الحذف بيكون عند المزرعه
    public void deletePlant(Integer farmId, Integer plantId) {
        Farm farm=farmRepository.findFarmById(farmId);
        Plant plant = plantRepository.findPlantById(plantId);
        if (farm==null||plant == null) {
            throw new ApiException("Plant with ID: " + plantId + " can not delete");
        }
        plantRepository.delete(plant);
    }
    // CRUD - End

    // Services
    /// Ebtehal
    public void transferPlantBetweenFarms(Integer farm1, Integer farm2, Integer ownerId, String type, Integer quantityToTransfer) {
        // العثور على العميل باستخدام ownerId
        Customer customer = customerRepository.findCustomerById(ownerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        boolean ownsFarm1 = false;
        boolean ownsFarm2 = false;

        for (Farm farm : customer.getFarms()) {
            if (farm.getId().equals(farm1)) ownsFarm1 = true;
            if (farm.getId().equals(farm2)) ownsFarm2 = true;
        }

        if (!ownsFarm1 || !ownsFarm2) {
            throw new ApiException("You must be the owner of both farms");
        }

        // العثور على النبات في المزرعة الأولى
        Optional<Plant> plantInFarm1Opt = plantRepository.findByFarm_IdAndType(farm1, type);
        if (!plantInFarm1Opt.isPresent()) {
            throw new ApiException("Plant not found in source farm");
        }

        Plant plantInFarm1 = plantInFarm1Opt.get();

        // التحقق من كمية النبات في المزرعة الأولى
        if (plantInFarm1.getQuantity() < quantityToTransfer) {
            throw new ApiException("Not enough quantity to transfer");
        }

        // العثور على النبات في المزرعة الثانية أو إنشاؤه
        Optional<Plant> plantInFarm2Opt = plantRepository.findByFarm_IdAndType(farm2, type);
        Plant plantInFarm2;

        if (plantInFarm2Opt.isPresent()) {
            plantInFarm2 = plantInFarm2Opt.get();
        } else {
            Farm destinationFarm = farmRepository.findFarmById(farm2);
            if (destinationFarm == null) {
                throw new ApiException("Destination farm not found");
            }
            plantInFarm2 = new Plant();
            plantInFarm2.setFarm(destinationFarm);
            plantInFarm2.setType(type);
            plantInFarm2.setQuantity(0);
        }

        // تحديث الكمية في المزرعتين
        plantInFarm1.setQuantity(plantInFarm1.getQuantity() - quantityToTransfer);
        plantInFarm2.setQuantity(plantInFarm2.getQuantity() + quantityToTransfer);

        // حفظ التحديثات
        plantRepository.save(plantInFarm1);
        plantRepository.save(plantInFarm2);
    }

}
