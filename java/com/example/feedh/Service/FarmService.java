package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.FarmDTOout;
import com.example.feedh.DTOout.FarmerDTOout;
import com.example.feedh.DTOout.LiveStockDTOout;
import com.example.feedh.DTOout.PlantDTOout;
import com.example.feedh.Model.*;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Nawaf - Farm Service
@Service
@RequiredArgsConstructor
public class FarmService {
    private final CustomerRepository customerRepository;
    private final FarmRepository farmRepository;

    // CRUD - Start
    public List<FarmDTOout> getAllFarms() {
        List<Farm> farms = farmRepository.findAll();  // الحصول على جميع المزارع
        List<FarmDTOout> farmDTOS = new ArrayList<>();

        // المرور على كل المزارع
        for (Farm f : farms) {
            // إنشاء قائمة للـ FarmerDTO
            List<FarmerDTOout> farmerDTOS = new ArrayList<>();
            for (Farmer farmer : f.getFarmers()) {
                farmerDTOS.add(new FarmerDTOout(farmer.getName(), farmer.getPhoneNumber(), farmer.getAddress(), farmer.getVisaType()));
            }

            // إنشاء قائمة للـ PlantDTO
            List<PlantDTOout> plantDTOS = new ArrayList<>();
            for (Plant p : f.getPlants()) {
                plantDTOS.add(new PlantDTOout(p.getType(), p.getQuantity()));
            }

            // إنشاء قائمة للـ LiveStockDTO
            List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
            for (LiveStock ls : f.getLiveStocks()) {
                liveStockDTOS.add(new LiveStockDTOout(ls.getType(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
            }

            // تحويل Farm إلى FarmDTO
            farmDTOS.add(new FarmDTOout(f.getName(), f.getLocation(), f.getSize(), f.getType(), farmerDTOS, plantDTOS, liveStockDTOS));
        }
        return farmDTOS;
    }

    public void addFarm(Integer customerId, Farm farm) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        if (!customer.getFoundationFile()) {
            throw new ApiException("Customer with ID: " + customerId + " cannot own a farm, they have no foundation file");
        }
        farm.setCustomer(customer);
        farmRepository.save(farm);
    }

    public void updateFarm(Integer farmId, Farm farm){
        Farm oldFarm = farmRepository.findFarmById(farmId);
        if (oldFarm == null){
            throw  new ApiException("Farm with ID: " + farmId + " was not found");
        }
        oldFarm.setName(farm.getName());
        oldFarm.setLocation(farm.getLocation());
        oldFarm.setSize(farm.getSize());
        oldFarm.setType(farm.getType());


        farmRepository.save(oldFarm);
    }

    public void deleteFarm(Integer ownerId,Integer farmId) {
        Customer customer = customerRepository.findCustomerById(ownerId);
        Farm farm = farmRepository.findFarmById(farmId);
        if (customer==null||farm == null) {
            throw new ApiException("You can not delete the farm");
        }
        farmRepository.delete(farm);
    }
    // CRUD - End

    // Services
    public List<Farm>getFarmByLocation(Integer customerId,Integer farmId ,String location){
        Customer customer=customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        Farm farm=farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }
        List<Farm>farmList= farmRepository.findFarmByLocation(location);

        if(farmList.isEmpty()){
            throw new ApiException("There no Farms in this location");
        }

        return farmList;
    }

    public List<FarmDTOout> getFarmByCustomer(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        List<Farm> farms = farmRepository.findFarmByCustomer(customer);
        List<FarmDTOout> farmDTOS = new ArrayList<>();
        for (Farm f : farms) {
            List<FarmerDTOout> farmerDTOS = new ArrayList<>();
            for (Farmer farmer : f.getFarmers()) {
                farmerDTOS.add(new FarmerDTOout(farmer.getName(), farmer.getPhoneNumber(), farmer.getAddress(), farmer.getVisaType()));
            }

            List<PlantDTOout> plantDTOS = new ArrayList<>();
            for (Plant p : f.getPlants()) {
                plantDTOS.add(new PlantDTOout(p.getType(), p.getQuantity()));
            }

            List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
            for (LiveStock ls : f.getLiveStocks()) {
                liveStockDTOS.add(new LiveStockDTOout(ls.getType(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
            }

            farmDTOS.add(new FarmDTOout(f.getName(), f.getLocation(), f.getSize(), f.getType(), farmerDTOS, plantDTOS, liveStockDTOS));
        }
        return farmDTOS;
    }

    public List<FarmDTOout> getFarmByCustomerAndType(Integer customerId, String type) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        List<Farm> farms = farmRepository.findFarmByCustomerAndType(customer, type);
        List<FarmDTOout> farmDTOS = new ArrayList<>();
        for (Farm f : farms) {
            List<FarmerDTOout> farmerDTOS = new ArrayList<>();
            for (Farmer farmer : f.getFarmers()) {
                farmerDTOS.add(new FarmerDTOout(farmer.getName(), farmer.getPhoneNumber(), farmer.getAddress(), farmer.getVisaType()));
            }

            List<PlantDTOout> plantDTOS = new ArrayList<>();
            for (Plant p : f.getPlants()) {
                plantDTOS.add(new PlantDTOout(p.getType(), p.getQuantity()));
            }

            List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
            for (LiveStock ls : f.getLiveStocks()) {
                liveStockDTOS.add(new LiveStockDTOout(ls.getType(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
            }

            farmDTOS.add(new FarmDTOout(f.getName(), f.getLocation(), f.getSize(), f.getType(), farmerDTOS, plantDTOS, liveStockDTOS));
        }
        return farmDTOS;
    }
}