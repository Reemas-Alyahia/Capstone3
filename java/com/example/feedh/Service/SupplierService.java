package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.DTOoutSUP;
import com.example.feedh.DTOout.HeavyEquipmentDTOout;
import com.example.feedh.DTOout.ProductDTOout;
import com.example.feedh.DTOout.SupplierDTOout;
import com.example.feedh.Model.*;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.ProductRepository;
import com.example.feedh.Repository.RentalRepository;
import com.example.feedh.Repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Ebtehal - Supplier Service
@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final RentalRepository rentalRepository;
    private final JavaMailSender mailSender;
    // CRUD - Start
    public List<SupplierDTOout> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierDTOout> supplierDTOS = new ArrayList<>();

        for (Supplier s : suppliers) {
            // ProductDTO
            List<ProductDTOout> productDTOS = new ArrayList<>();
            for (Product p : s.getProducts()) {
                productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
            }

            // HeavyEquipmentDTO
            List<HeavyEquipmentDTOout> heavyEquipmentDTOS = new ArrayList<>();
            for (HeavyEquipment he : s.getHeavyEquipments()) {
                heavyEquipmentDTOS.add(new HeavyEquipmentDTOout(he.getName(), he.getPricePerHour(), he.getInsurance(), he.getStatus()));
            }
            // Adding SupplierDTO to the list
            supplierDTOS.add(new SupplierDTOout(s.getName(), s.getEmail(), s.getPhoneNumber(), s.getAddress(), productDTOS, heavyEquipmentDTOS));
        }
        return supplierDTOS;
    }

    public void addSupplier(Supplier supplier) {
        if (!supplier.getFoundationFile()) {
            throw new ApiException("Supplier with ID: " + supplier.getId() + " cannot register because they don't have foundation file");
        }
        supplierRepository.save(supplier);
    }

    public void updateSupplier(Integer supplierId, Supplier supplier) {
        Supplier oldSupplier = supplierRepository.findSupplierById(supplierId);
        if (oldSupplier == null) {
            throw new ApiException("Supplier with ID: " + supplierId + " was not found");
        }
        oldSupplier.setName(supplier.getName());
        oldSupplier.setEmail(supplier.getEmail());
        oldSupplier.setPhoneNumber(supplier.getPhoneNumber());
        oldSupplier.setAddress(supplier.getAddress());
        oldSupplier.setPassword(supplier.getPassword());
        oldSupplier.setCompanyRegister(supplier.getCompanyRegister());
        supplierRepository.save(oldSupplier);
    }

    public void deleteSupplier(Integer supplierId) {
        Supplier supplier = supplierRepository.findSupplierById(supplierId);
        if (supplier == null) {
            throw new ApiException("Supplier with ID: " + supplierId + " was not found");
        }
        supplierRepository.delete(supplier);
    }
    // CRUD - End

    // Services
    //Eb
    public List<Supplier>getSupplierByAddress(String address){
            List<Supplier>suppliers=supplierRepository.findSupplierByAddress(address);
            if (suppliers==null){
                throw new ApiException("there is no suppliers in that address");
            }
        return suppliers;
    }

    //Ebtehal
    public List<Rental> getRentalsNearExpiration() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime nearEndDate = today.plusDays(3);

        List<Rental> rentalsNearExpiration = rentalRepository.findByEndDateTimeBetween(today, nearEndDate);

        for (Rental rental : rentalsNearExpiration) {
            if (rental.getCustomer() != null && rental.getCustomer().getEmail() != null) {
                sendEmailNotification(rental.getCustomer().getEmail(), rental);
            }
        }

        return rentalsNearExpiration;
    }

    // Ebtehal
    //     إرسال بريد إلكتروني للعميل
    private void sendEmailNotification(String email, Rental rental) {
        String subject = "Rental Contract Expiration Notice";
        String message = "Dear Customer,\n\n"
                + "This is a reminder that your rental contract (ID: " + rental.getId() + ") "
                + "for the heavy equipment will expire on " + rental.getEndDateTime() + ".\n"
                + "Please contact us if you need to renew or extend your contract.\n\n"
                + "Thank you,\nYour Farm Management Team";

        // إنشاء وإرسال الرسالة
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    //Ebtehal
    // get Supplier by ProductPrice
    public List<DTOoutSUP> getSuppliersByProductPrice(Double price) {
        List<Supplier> suppliers = supplierRepository.findSuppliersByProductPrice(price);
        List<DTOoutSUP> dtoList = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            DTOoutSUP dto = new DTOoutSUP(
                    supplier.getName(),
                    supplier.getEmail(),
                    supplier.getPhoneNumber(),
                    supplier.getAddress()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }

    //Ebtehal
    // get Supplier by heavyEquepment Price
    public List<DTOoutSUP> getSuppliersByHeavyEquipmentRentPrice(Double price) {
        List<Supplier> suppliers = supplierRepository.findSuppliersByHeavyEquipmentRentPrice(price);
        List<DTOoutSUP> dtoList = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            DTOoutSUP dto = new DTOoutSUP(
                    supplier.getName(),
                    supplier.getEmail(),
                    supplier.getPhoneNumber(),
                    supplier.getAddress()
            );
            dtoList.add(dto);
        }
        return dtoList;
    }
}