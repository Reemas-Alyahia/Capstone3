package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.RentalDTOout;
import com.example.feedh.Model.*;
import com.example.feedh.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Reemas - Rental Service
@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final FarmerRepository farmerRepository;
    private final HeavyEquipmentRepository heavyEquipmentRepository;
    private final HeavyEquipmentService heavyEquipmentService;
    private final EmailService emailService;
    private final SupplierRepository supplierRepository;

    // CRUD - Start
    public List<RentalDTOout> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<RentalDTOout> rentalDTOS = new ArrayList<>();

        for (Rental r : rentals) {
            rentalDTOS.add(new RentalDTOout(r.getStartDateTime(), r.getEndDateTime(), r.getPrice(), r.getStatus()));
        }
        return rentalDTOS;
    }

//    public void addRental(Integer customerId,Integer farmer_id,Integer heavyEquipment_id ,Rental rental) {
//        Customer customer = customerRepository.findCustomerById(customerId);
//        if (customer == null) {
//            throw new ApiException("Customer with ID: " + customerId + " was not found");
//        }
//
//        Farmer farmer=farmerRepository.findFarmerById(farmer_id);
//        if(farmer==null){
//            throw new ApiException("Farmer with ID: " + farmer_id + " was not found");
//        }
//        if(!farmer.getVisaType().equalsIgnoreCase("Shepherd")){
//            throw new ApiException("Sorry but the farmer must have Shepherd to use the Heavy Equipment");
//        }
//        HeavyEquipment heavy=heavyEquipmentRepository.findHeavyEquipmentById(heavyEquipment_id);
//        if(heavy==null){
//            throw new ApiException(" The Heavy Equipment with ID: " + heavy + " was not found");
//        }
//        if(heavy.getStatus().equalsIgnoreCase("Rented")){
//            throw new ApiException("Sorry but the Heavy Equipment is not Available");
//        }
//        heavy.setStatus("Rented");
//        heavyEquipmentRepository.save(heavy);
//        Double newPrice = checkRentalHistoryAndNotifyDiscount(heavy.getSupplier().getId(),customerId,heavy);
//        rental.setCustomer(customer);
//        rental.setPrice(calculateRentalPrice(rental.getStartDateTime(), rental.getEndDateTime(), newPrice));
//        rentalRepository.save(rental);
//    }
    public void addRental(Integer customerId, Integer farmerId, Integer heavyEquipmentId, Rental rental) {

        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        Farmer farmer = farmerRepository.findFarmerById(farmerId);
        if (farmer == null) {
            throw new ApiException("Farmer with ID: " + farmerId + " was not found");
        }
        if (!farmer.getVisaType().equalsIgnoreCase("Shepherd")) {
            throw new ApiException("Sorry but the farmer must have Shepherd visa to use the Heavy Equipment");
        }

        HeavyEquipment heavy = heavyEquipmentRepository.findHeavyEquipmentById(heavyEquipmentId);
        if (heavy == null) {
            throw new ApiException("The Heavy Equipment with ID: " + heavyEquipmentId + " was not found");
        }
        if (heavy.getStatus().equalsIgnoreCase("Rented")) {
            throw new ApiException("Sorry but the Heavy Equipment is not Available");
        }

        heavy.setStatus("Rented");
        heavyEquipmentRepository.save(heavy);

        rental.setPrice(calculateRentalPrice(rental.getStartDateTime(),rental.getEndDateTime(),heavy.getPricePerHour()) + heavy.getInsurance());
        rental.setCustomer(customer);
        rentalRepository.save(rental);

        // Send Email with equipment details and price
        sendRentalConfirmationEmail(customer, heavy,rental);
    }

    // Reemas - Notification method used in 'addRental' to notify a customer then they rent a heavy equipment
    private void sendRentalConfirmationEmail(Customer customer,HeavyEquipment heavy, Rental rental) {
        String userEmail = customer.getEmail();
        String subject = "Rental Confirmation: " + heavy.getName();
        String body = "Dear " + customer.getName() + ",\n\n"
                + "Thank you for renting the heavy equipment. Below are the details of your rental:\n\n"
                + "Equipment Name: " + heavy.getName() + "\n"
                + "Rental Price: " +  calculateRentalPrice(rental.getStartDateTime(), rental.getEndDateTime(), heavy.getPricePerHour()) +  " SAR\n"
                + "Heavy Equipment Insurance: " + heavy.getInsurance() + "\n"
                + "Rental Start Date: " +rental.getStartDateTime()  + "\n"
                + "Rental End Date: " + rental.getEndDateTime() + "\n\n"
                + "If you have any questions, feel free to contact us.\n\n"
                + "Best regards,\n"
                + "Your Equipment Rental Team";

        // Send email using the email service
        emailService.sendEmail(userEmail, subject, body);
    }

    public void updateRental(Integer rentalId, Rental rental) {
        Rental oldRental = rentalRepository.findRentalById(rentalId);
        if (oldRental == null) {
            throw new ApiException("Rental with ID: " + rentalId + " was not found");
        }
        oldRental.setStartDateTime(rental.getStartDateTime());
        oldRental.setEndDateTime(rental.getEndDateTime());
        oldRental.setPrice(rental.getPrice());
        oldRental.setStatus(rental.getStatus());
        rentalRepository.save(oldRental);
    }
    // CRUD - End


    // Services
    /// //2 calculateRentalPrice
    public Double calculateRentalPrice(LocalDateTime rentalStartDate, LocalDateTime rentalEndDate, Double price) {
        if (rentalEndDate.isBefore(rentalStartDate)) {
            throw new IllegalArgumentException("Rental end date must be after rental start date.");
        }

        long hours = ChronoUnit.HOURS.between(rentalStartDate, rentalEndDate);

        Double totalRentalPrice = hours * price;

        return totalRentalPrice;
    }

    /// /3 end
    @Scheduled(fixedRate = 60000) // Runs every 10 minutes
    public void updateRentalStatus(){
        LocalDateTime now = LocalDateTime.now();
        List<Rental> expiredRentals = rentalRepository.getRentalByRentalEndDateAndStatus(now, "Active");

        for (Rental r : expiredRentals) {
            r.setStatus("Completed");


            for (HeavyEquipment he : r.getHeavyEquipments()) {
                he.setStatus("Available");
                heavyEquipmentRepository.save(he);
            }

            rentalRepository.save(r);

            sendRentalCompletionEmail(r);
        }
    }


    private void sendRentalCompletionEmail(Rental rental) {
        Customer customer = rental.getCustomer();
        String email = customer.getEmail();
        String subject = "Rental Contract Completion Notification";
        String message = "Dear " + customer.getName() + ",\n\n"
                + "This is to notify you that your rental contract (ID: " + rental.getId() + ") "
                + "for the heavy equipment has been completed.\n"
                + "The equipment is now available for new rentals.\n\n"
                + "Thank you for choosing us.\n\n"
                + "Best regards,\nYour Rental Team";

        emailService.sendEmail(email, subject, message);
    }

    public List<Rental>getRentalByPriceAndStatus(Integer rentalId,Double price,String status){
        Rental rental=rentalRepository.findRentalById(rentalId);
        if (rental == null) {
            throw new ApiException("Rental with ID: " + rentalId + " was not found");
        }
        List<Rental>rentals=rentalRepository.getRentalByPriceAndStatus(price,status);
        if(rentals.isEmpty()){
            throw new ApiException("Sorry but the rentals is empty");
        }
        return  rentals;
    }

//    public Double checkRentalHistoryAndNotifyDiscount(Customer customer, Supplier supplier, HeavyEquipment heavyEquipment) {
//        Integer rentalCount = rentalRepository.countRentalsByCustomerAndSupplier(customer.getId(), supplier.getId());
//        System.out.println("Rental count for customer " + customer.getId() + " and supplier " + supplier.getId() + ": " + rentalCount);
//
//        if (rentalCount >= 3) {
//            sendDiscountNotification(customer, supplier);
//            return heavyEquipment.getPricePerHour() * (1 - 0.5); // Apply discount
//        }
//
//        return heavyEquipment.getPricePerHour();
//    }

//    private void sendDiscountNotification(Customer customer, Supplier supplier) {
//        String email = customer.getEmail();
//        if (email == null) {
//            throw new ApiException("Customer email is not available");
//        }
//
//        String subject = "Special Discount for Valued Customer!";
//        String body = "Dear " + customer.getName() + ",\n\n"
//                + "Thank you for your continued trust in our services.\n"
//                + "As a valued customer who has rented from " + supplier.getName() + " multiple times, "
//                + "we are pleased to offer you a 50% discount on your next rental.\n\n"
//                + "Don't miss this opportunity to save big!\n\n"
//                + "Best regards,\n"
//                + "Your Farm Management Team";
//
//        emailService.sendEmail(email, subject, body);
//    }

}
