package com.example.feedh.Service;

import com.example.feedh.ApiResponse.ApiException;
import com.example.feedh.DTOout.ProductDTOout;
import com.example.feedh.Model.Customer;
import com.example.feedh.Model.OrderFD;
import com.example.feedh.Model.Product;
import com.example.feedh.Model.Supplier;
import com.example.feedh.Repository.CustomerRepository;
import com.example.feedh.Repository.OrderFDRepository;
import com.example.feedh.Repository.ProductRepository;
import com.example.feedh.Repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Reemas - Product Service
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final OrderFDRepository orderFDRepository;
    private final EmailService emailService;

    // CRUD - Start
    public List<ProductDTOout> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTOout> productDTOS = new ArrayList<>();

        for (Product p : products) {
            productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
        }
        return productDTOS;
    }

    public void addProduct(Integer supplierId, Product product) {
        Supplier supplier = supplierRepository.findSupplierById(supplierId);
        if (supplier == null) {
            throw new ApiException("Supplier with ID: " + supplierId + " was not found");
        }
        product.setSupplier(supplier);
        productRepository.save(product);
    }

    public void updateProduct(Integer productId, Integer supplierId, Product product) {
        Supplier supplier = supplierRepository.findSupplierById(supplierId);
        if (supplier == null) {
            throw new ApiException("Supplier with ID: " + supplierId + " was not found");
        }

        Product oldProduct = productRepository.findProductById(productId);
        if (oldProduct == null) {
            throw new ApiException("Product with ID: " + productId + " was not found");
        }
        oldProduct.setName(product.getName());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        productRepository.save(oldProduct);
    }

    public void deleteProduct(Integer productId, Integer supplierId) {
        Supplier supplier = supplierRepository.findSupplierById(supplierId);
        if (supplier == null) {
            throw new ApiException("Supplier with ID: " + supplierId + " was not found");
        }

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product with ID: " + productId + " was not found");
        }
        productRepository.delete(product);
    }
    // CRUD - End

    // Services
    public ProductDTOout getProductById(Integer productId) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product with ID: " + productId + " was not found");
        }
        return new ProductDTOout(product.getName(), product.getCategory(), product.getDescription(), product.getPrice(), product.getQuantity());
    }

    public List<ProductDTOout> getProductByCategory(String category) {
        List<Product> products = productRepository.findProductByCategory(category);
        if (products.isEmpty()) {
            throw new ApiException("There are no products in this category");
        }
        List<ProductDTOout> productDTOS = new ArrayList<>();
        for (Product p : products) {
            productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
        }
        return productDTOS;
    }

    public List<ProductDTOout> getProductByPriceMoreThanOrEqual(Double price) {
        List<Product> products = productRepository.getProductByPriceMoreThanOrEqual(price);
        if (products.isEmpty()) {
            throw new ApiException("There are no products with price more than " + price);
        }
        List<ProductDTOout> productDTOS = new ArrayList<>();
        for (Product p : products) {
            productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
        }
        return productDTOS;
    }

    public List<ProductDTOout> getProductByPriceLessThanOrEqual(Double price) {
        List<Product> products = productRepository.getProductByPriceLessThanOrEqual(price);
        if (products.isEmpty()) {
            throw new ApiException("There are no products with price less than " + price);
        }
        List<ProductDTOout> productDTOS = new ArrayList<>();
        for (Product p : products) {
            productDTOS.add(new ProductDTOout(p.getName(), p.getCategory(), p.getDescription(), p.getPrice(), p.getQuantity()));
        }
        return productDTOS;
    }
    //Ebtehal
    public List<ProductDTOout> getLowQuantityProductsForSupplier(Integer supplierId) {
        List<Product> products = productRepository.findLowQuantityProductsBySupplierId(supplierId, 5);
        if (products == null || products.isEmpty()) {
            throw new ApiException("There are no products found with quantity less than 5");
        }

        // تحويل المنتجات إلى قائمة من ProductDTOout
        List<ProductDTOout> productDTOoutList = new ArrayList<>();
        for (Product product : products) {
            ProductDTOout dto = new ProductDTOout();
            dto.setName(product.getName());
            dto.setCategory(product.getCategory());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            productDTOoutList.add(dto);
        }

        return productDTOoutList;
    }

    public void buyProductById(Integer customerId, Integer productId, Integer quantity) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID: " + customerId + " was not found");
        }

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product with ID: " + productId + " was not found");
        }

        if (product.getQuantity() < quantity) {
            throw new ApiException("Not enough stock for product: " + product.getName() + ". Available quantity: " + product.getQuantity());
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        OrderFD order = new OrderFD();
        order.setCustomer(customer);
        order.setProducts(Set.of(product));
        order.setQuantity(quantity);
        order.setTotalAmount(product.getPrice() * quantity);
        order.setStatus("Completed");
        orderFDRepository.save(order);
        sendPurchaseNotification(customer, product, quantity);
    }

    public void applyDiscountToProduct(Integer supplierId, Integer productId, Double discount) {
        Product product = productRepository.findProductById(productId);
        if (product==null){
            throw new ApiException("product not found");
        }


        Supplier supplier = product.getSupplier();
        if (supplier == null || !supplier.getId().equals(supplierId)) {
            throw new ApiException("You are not authorized to modify this product");
        }

        if (discount < 0 || discount > 100) {
            throw new ApiException("Discount must be between 0 and 100");
        }

        Double originalPrice = product.getPrice();
        Double discountedPrice = originalPrice * (1 - discount / 100);
        product.setPrice(discountedPrice);

        productRepository.save(product);
    }

    // Nawaf - Notification method used to notify the customers when they purchase a product
    private void sendPurchaseNotification(Customer customer, Product product, Integer quantity) {
        String email = customer.getEmail();
        if (email == null) {
            throw new ApiException("Customer email is not available");
        }

        String subject = "Purchase Confirmation - Thank You for Your Order!";
        String body = String.format(
                "Dear %s,\n\n" +
                        "Thank you for your recent purchase! We are delighted to confirm your order. Below are the details of your purchase:\n\n" +
                        "Product Details:\n" +
                        "- Product Name: %s\n" +
                        "- Quantity: %d\n" +
                        "- Price per Unit: %.2f\n" +
                        "- Total Amount: %.2f\n\n" +
                        "Your order has been successfully processed and is now marked as 'Completed'. If you have any questions, feel free to contact us.\n\n" +
                        "We appreciate your business and look forward to serving you again!\n\n" +
                        "Best regards,\n" +
                        "Your Farm Management Team",
                customer.getName(), product.getName(), quantity, product.getPrice(), product.getPrice() * quantity
        );
        emailService.sendEmail(email, subject, body);
    }
}
