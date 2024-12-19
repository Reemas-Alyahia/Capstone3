package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.OrderFDDTOout;
import com.example.feedh.DTOout.ProductDTOout;
import com.example.feedh.Model.Customer;
import com.example.feedh.Model.OrderFD;
import com.example.feedh.Model.Product;
import com.example.feedh.Repository.AdminRepository;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.OrderFDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Reemas - OrderFD Service
@Service
@RequiredArgsConstructor
public class OrderFDService {
    private final OrderFDRepository orderFDRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final EmailService emailService;

    // CRUD - Start
    public List<OrderFDDTOout> getAllOrders() {
        List<OrderFD> orderFDS = orderFDRepository.findAll();
        List<OrderFDDTOout> orderFDDTOS = new ArrayList<>();

        for (OrderFD o : orderFDS) {
            List<ProductDTOout> productDTOS = new ArrayList<>();
            for (Product p : o.getProducts()) {
                productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
            }
            orderFDDTOS.add(new OrderFDDTOout(o.getOrderDateTime(), o.getQuantity(), o.getTotalAmount(), o.getStatus(), productDTOS));
        }
        return orderFDDTOS;
    }

    public void addOrder(Integer customerId, OrderFD orderFD) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        if (orderFD.getTotalAmount() > checkAverageOrders(customerId)) {
            sendNotification(customer);
        }
        orderFD.setCustomer(customer);
        orderFDRepository.save(orderFD);
    }

    public void updateOrder(Integer orderId, OrderFD orderFD) {
        OrderFD oldOrderFD = orderFDRepository.findOrderFDById(orderId);
        if (oldOrderFD == null) {
            throw new ApiException("Order with ID: " + orderId + " was not found");
        }
        oldOrderFD.setOrderDateTime(orderFD.getOrderDateTime());
        oldOrderFD.setQuantity(orderFD.getQuantity());
        oldOrderFD.setTotalAmount(orderFD.getTotalAmount());
        oldOrderFD.setStatus(orderFD.getStatus());
        orderFDRepository.save(oldOrderFD);
    }

    public void deleteOrder(Integer orderId) {
        OrderFD orderFD = orderFDRepository.findOrderFDById(orderId);
        if (orderFD == null) {
            throw new ApiException("Order with ID: " + orderId + " was not found");
        }
        orderFDRepository.delete(orderFD);
    }
    // CRUD - End

    // Services
    // Reemas - Nawaf: Helper method used in 'addOrder' to check the average of customer's orders, and detects if an item bought with more amount than average, then sends an email!
    public Double checkAverageOrders(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }
        Double avg = 0.0;
        for (OrderFD o : customer.getOrders()) {
            avg += o.getTotalAmount();
        }
        avg /= customer.getOrders().size();
        return avg;
    }

    public List<OrderFDDTOout> getOrderHistoryByCustomer(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        List<OrderFD> orders = orderFDRepository.findOrderFDByCustomer(customer);
        if (orders.isEmpty()) {
            throw new ApiException("There are no orders yet");
        }
        List<OrderFDDTOout> orderFDDTOS = new ArrayList<>();
        for (OrderFD o : orders) {
            List<ProductDTOout> productDTOS = new ArrayList<>();
            for (Product p : o.getProducts()) {
                productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
            }
            orderFDDTOS.add(new OrderFDDTOout(o.getOrderDateTime(), o.getQuantity(), o.getTotalAmount(), o.getStatus(),productDTOS));
        }
        return orderFDDTOS;
    }

    // Reemas - Helper method used to send an email
    private void sendNotification(Customer customer) {
        String userEmail = customer.getEmail();
        String subject = "Notice of Unusual Purchase Activity on Your Account";
        String body = "Dear " + customer.getName() + ",\n" +
                "\n" +
                "We hope this message finds you well.\n" +
                "\n" +
                "We would like to bring to your attention a recent purchase activity on your account that appears unusual due to its high value. \n" +
                "Thank you for choosing us.\n" +
                "\n" +
                "Best regards,";
        emailService.sendEmail(userEmail, subject, body);
    }
}
