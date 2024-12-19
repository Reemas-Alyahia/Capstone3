package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.FarmerDTOout;
import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Farm;
import com.example.feedh.Model.Farmer;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.FarmRepository;
import com.example.feedh.Repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Nawaf - Farmer Service
@Service
@RequiredArgsConstructor
public class FarmerService {
    private final FarmerRepository farmerRepository;
    private final CustomerRepository customerRepository;
    private final FarmRepository farmRepository;

    public List<FarmerDTOout> getAllFarmers() {
        List<Farmer> farmers = farmerRepository.findAll();
        List<FarmerDTOout> farmerDTOS = new ArrayList<>();

        for (Farmer f : farmers) {
            farmerDTOS.add(new FarmerDTOout(f.getName(), f.getPhoneNumber(), f.getAddress(), f.getVisaType()));
        }
        return farmerDTOS;
    }

    public void addFarmer(Integer farmId, Integer customerId, Farmer farmer) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }

        if (!farmer.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You don't have permission on this farmer");
        }

        if (isFarmerEligible(farm, farmer)) {
            farmer.setCustomer(customer);
            farmer.setFarm(farm);
            farmerRepository.save(farmer);
        }
        else {
            throw new ApiException("Invalid assignment: The farmer's role is not suitable for the selected farm type");
        }
    }
    //Nawaf - Helper method for the above service
    private boolean isFarmerEligible(Farm farm, Farmer farmer) {
        String farmType = farm.getType();
        String visaType = farmer.getVisaType();

        return (farmType.equals("Agricultural") && visaType.equals("Farmer")) ||
                (farmType.equals("Livestock") && visaType.equals("Shepherd")) ||
                farmType.equals("Mixed");
    }

    public void updateFarmer(Integer farmerId, Integer customerId, Farmer farmer) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        Farmer oldFarmer = farmerRepository.findFarmerById(farmerId);
        if (oldFarmer == null) {
            throw new ApiException("Farmer with ID: " + farmerId + " was not found");
        }

        if (!farmer.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You don't have permission on this farmer");
        }

        oldFarmer.setName(farmer.getName());
        oldFarmer.setPhoneNumber(farmer.getPhoneNumber());
        oldFarmer.setAddress(farmer.getAddress());
        oldFarmer.setPassword(farmer.getPassword());
        oldFarmer.setVisaType(farmer.getVisaType());
        farmerRepository.save(oldFarmer);
    }

    public void deleteFarmer(Integer farmerId, Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer==null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        Farmer farmer = farmerRepository.findFarmerById(farmerId);
        if (farmer == null) {
            throw new ApiException("Farmer with ID: " + farmerId + " was not found");
        }

        if (!farmer.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You don't have permission on this farmer");
        }

        farmerRepository.delete(farmer);
    }

    public void transferFarmer(Integer farmerId, Integer farm1Id, Integer farm2Id, Integer customerId) {
        Farm farm1 = farmRepository.findFarmById(farm1Id);
        if (farm1 == null) {
            throw new ApiException("Farm 1 not found");
        }

        Farm farm2 = farmRepository.findFarmById(farm2Id);
        if (farm2 == null) {
            throw new ApiException("Farm 2 not found");
        }

        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null || !(customer.getFarms().contains(farm1) && customer.getFarms().contains(farm2))) {
            throw new ApiException("You must be the owner of both farms");
        }


        Farmer farmer = farmerRepository.findFarmerById(farmerId);
        if (farmer == null) {
            throw new ApiException("Farmer not found");
        }

        if (!farmer.getFarm().equals(farm1)) {
            throw new ApiException("Farmer is not assigned to farm 1");
        }

        if (farm1.equals(farm2)) {
            throw new ApiException("Cannot transfer farmer to the same farm");
        }
        farm2.getFarmers().add(farmer);
        farm1.getFarmers().remove(farmer);
        farmRepository.save(farm1);
        farmRepository.save(farm2);

        farmer.setFarm(farm2);
        farmerRepository.save(farmer);
    }

    public List<FarmerDTOout> getFarmerByCustomer(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        List<Farmer> farmers = farmerRepository.findFarmerByCustomer(customer);
        if (farmers.isEmpty()) {
            throw new ApiException("There are no farmers registered on this customer");
        }

        List<FarmerDTOout> farmerDTOS = new ArrayList<>();
        for (Farmer f : farmers) {
            farmerDTOS.add(new FarmerDTOout(f.getName(), f.getPhoneNumber(), f.getAddress(), f.getVisaType()));
        }
        return farmerDTOS;
    }

    public List<FarmerDTOout> getFarmerByCustomerAndFarm(Integer customerId, Integer farmId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }

        List<Farmer> farmers = farmerRepository.findFarmerByCustomerAndFarm(customer, farm);
        if (farmers.isEmpty()) {
            throw new ApiException("There are no farmers yet");
        }

        List<FarmerDTOout> farmerDTOS = new ArrayList<>();
        for (Farmer f : farmers) {
            farmerDTOS.add(new FarmerDTOout(f.getName(), f.getPhoneNumber(), f.getAddress(), f.getVisaType()));
        }
        return farmerDTOS;
    }

    public List<FarmerDTOout> getFarmerByCustomerAndVisaType(Integer customerId, String visaType) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        List<Farmer> farmers = farmerRepository.findFarmerByCustomerAndVisaType(customer, visaType);
        if (farmers.isEmpty()) {
            throw new ApiException("There are no farmers yet");
        }

        List<FarmerDTOout> farmerDTOS = new ArrayList<>();
        for (Farmer f : farmers) {
            farmerDTOS.add(new FarmerDTOout(f.getName(), f.getPhoneNumber(), f.getAddress(), f.getVisaType()));
        }
        return farmerDTOS;
    }
}
