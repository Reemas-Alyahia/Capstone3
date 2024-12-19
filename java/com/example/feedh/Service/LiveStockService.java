package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.LiveStockDTOout;
import com.example.feedh.Model.Customer;
import com.example.feedh.Model.Farm;
import com.example.feedh.Model.LiveStock;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.FarmRepository;
import com.example.feedh.Repository.LiveStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Ebtehal - LiveStock Service
@Service
@RequiredArgsConstructor
public class LiveStockService {
    private final LiveStockRepository liveStockRepository;
    private final FarmRepository farmRepository;
    private final CustomerRepository customerRepository;

    // CRUD - Start
    public List<LiveStockDTOout> getAllLiveStocks() {
        List<LiveStock> liveStocks = liveStockRepository.findAll();
        List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();

        for (LiveStock ls : liveStocks) {
            liveStockDTOS.add(new LiveStockDTOout(ls.getType(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
        }
        return liveStockDTOS;
    }

    public void addLiveStock(Integer farmId, LiveStock liveStock) {
        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }
        liveStock.setFarm(farm);
        liveStockRepository.save(liveStock);
    }

    public void updateLiveStock(Integer liveStockId, LiveStock liveStock) {
        LiveStock oldLiveStock = liveStockRepository.findLiveStockById(liveStockId);
        if (oldLiveStock == null) {
            throw new ApiException("Live Stock with ID: " + liveStockId + " was not found");
        }
        oldLiveStock.setType(liveStock.getType());
        oldLiveStock.setBreed(liveStock.getBreed());
        oldLiveStock.setQuantity(liveStock.getQuantity());
        oldLiveStock.setFeedType(liveStock.getFeedType());
        liveStockRepository.save(oldLiveStock);
    }

    public void deleteLiveStock(Integer liveStockId) {
        LiveStock liveStock = liveStockRepository.findLiveStockById(liveStockId);
        if (liveStock == null) {
            throw new ApiException("Live Stock with ID: " + liveStockId + " was not found");
        }
        liveStockRepository.delete(liveStock);
    }
    // CRUD - End

    // Services
    public LiveStockDTOout getLiveStockById(Integer liveStockId) {
        LiveStock liveStock = liveStockRepository.findLiveStockById(liveStockId);
        if (liveStock == null) {
            throw new ApiException("Live Stock with ID: " + liveStockId + " was not found");
        }
        return new LiveStockDTOout(liveStock.getType(), liveStock.getBreed(), liveStock.getQuantity(), liveStock.getFeedType());
    }

    public List<LiveStockDTOout> getLiveStockByType(Integer customerId, Integer farmId, String type) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }

        List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
        for (Farm f : customer.getFarms()) {
            if (f.getId().equals(farmId)) {
                for (LiveStock ls : f.getLiveStocks()) {
                    if (ls.getType().equalsIgnoreCase(type)) {
                        liveStockDTOS.add(new LiveStockDTOout(ls.getType(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
                        return liveStockDTOS;
                    }
                }
                throw new ApiException("There are no live stock with this type");
            }
        }
        throw new ApiException("You don't own a farm with this ID: " + farmId);
    }

    public List<LiveStockDTOout> getLiveStockByBreed(Integer customerId, Integer farmId, String breed) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }

        List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
        for (Farm f : customer.getFarms()) {
            if (f.getId().equals(farmId)) {
                for (LiveStock ls : f.getLiveStocks()) {
                    if (ls.getBreed().equalsIgnoreCase(breed)) {
                        liveStockDTOS.add(new LiveStockDTOout(ls.getBreed(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
                        return liveStockDTOS;
                    }
                }
                throw new ApiException("There are no live stock with this breed");
            }
        }
        throw new ApiException("You don't own a farm with this ID: " + farmId);
    }

    public List<LiveStockDTOout> getLiveStockByQuantityGreaterThanEqual(Integer customerId, Integer farmId, Integer quantity) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }

        List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
        for (Farm f : customer.getFarms()) {
            if (f.getId().equals(farmId)) {
                for (LiveStock ls : f.getLiveStocks()) {
                    if (ls.getQuantity() >= quantity) {
                        liveStockDTOS.add(new LiveStockDTOout(ls.getBreed(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
                        return liveStockDTOS;
                    }
                }
                throw new ApiException("There are no live stock more than this quantity");
            }
        }
        throw new ApiException("You don't own a farm with this ID: " + farmId);
    }

    public List<LiveStockDTOout> getLiveStockByQuantityLessThanEqual(Integer customerId, Integer farmId, Integer quantity) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        Farm farm = farmRepository.findFarmById(farmId);
        if (farm == null) {
            throw new ApiException("Farm with ID: " + farmId + " was not found");
        }

        List<LiveStockDTOout> liveStockDTOS = new ArrayList<>();
        for (Farm f : customer.getFarms()) {
            if (f.getId().equals(farmId)) {
                for (LiveStock ls : f.getLiveStocks()) {
                    if (ls.getQuantity() <= quantity) {
                        liveStockDTOS.add(new LiveStockDTOout(ls.getBreed(), ls.getBreed(), ls.getQuantity(), ls.getFeedType()));
                        return liveStockDTOS;
                    }
                }
                throw new ApiException("There are no live stock less than this quantity");
            }
        }
        throw new ApiException("You don't own a farm with this ID: " + farmId);
    }

    /// // 4 reemas
    public Map<String,List<String>>  getFeedSuggestions(String type) {
        Map<String,List<String>> feedTypesByType = new HashMap<>();

        if (type.equalsIgnoreCase("Sheep")) {
            List<String> sheepFeeds = new ArrayList<>();
            sheepFeeds.add("Fresh Grass");
            sheepFeeds.add("Mixed Feed (e.g., grains and proteins)");
            sheepFeeds.add("Green Leaves (e.g., alfalfa, seasonal plants)");
            sheepFeeds.add("Higher Water Requirement");
            feedTypesByType.put("Sheep", sheepFeeds);
        }
        else if (type.equalsIgnoreCase("Camel")) {
            List<String> camelFeeds = new ArrayList<>();
            camelFeeds.add("Grain (e.g., barley, corn)");
            camelFeeds.add("Dry Forage (e.g., hay, dried alfalfa)");
            camelFeeds.add("Saline Plants (e.g., saltbush, atriplex)");
            camelFeeds.add("Limited Water Consumption");
            feedTypesByType.put("Camel", camelFeeds);
        } else {
            throw new ApiException("Invalid livestock type");}
        return feedTypesByType;
    }

    /// get LiveStock By Breed And Type, reemas
    public List<LiveStock>getLiveStockByBreedAndType(String breed, String type){
        List<LiveStock> liveStock = liveStockRepository.getLiveStockByBreedAndType(breed, type);
        if(liveStock.isEmpty()){
            throw new ApiException("There no LiveStock yet");
        }
        return liveStock;
    }
}
