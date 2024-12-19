package com.example.feedh.Controller;

import com.example.feedh.ApiResponse.ApiResponse;
import com.example.feedh.DTOout.ProductDTOout;
import com.example.feedh.Model.Product;
import com.example.feedh.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Reemas - Product Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    // CRUD - Start
    /// reemas
    @GetMapping("/get")
    public ResponseEntity getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAllProducts());
    }
    /// reemas
    @PostMapping("/add/{supplierId}")
    public ResponseEntity addProduct(@PathVariable Integer supplierId, @RequestBody @Valid Product product) {
        productService.addProduct(supplierId, product);
        return ResponseEntity.status(200).body(new ApiResponse("Product has been added to supplier with ID: " + supplierId + " successfully"));
    }
    /// reemas
    @PutMapping("/update/{productId}/{supplierId}")
    public ResponseEntity updateProduct(@PathVariable Integer productId, @PathVariable Integer supplierId, @RequestBody @Valid Product product) {
        productService.updateProduct(productId, supplierId, product);
        return ResponseEntity.status(200).body(new ApiResponse("Product with ID: " + productId + " has been updated successfully"));
    }
    /// reemas
    @DeleteMapping("/delete/{productId}/{supplierId}")
    public ResponseEntity deleteProduct(@PathVariable Integer productId, @PathVariable Integer supplierId) {
        productService.deleteProduct(productId, supplierId);
        return ResponseEntity.status(200).body(new ApiResponse("Product with ID: " + productId + " has been deleted successfully"));
    }
    // CRUD - End

    // Services
    // Nawaf - Endpoint returns a product by ID
    @GetMapping("/get/by-id/{productId}")
    public ResponseEntity getProductById(@PathVariable Integer productId) {
        return ResponseEntity.status(200).body(productService.getProductById(productId));
    }

    // Nawaf - Endpoint returns a list of products by category
    @GetMapping("/get/by-category/{category}")
    public ResponseEntity getProductByCategory(@PathVariable String category) {
        return ResponseEntity.status(200).body(productService.getProductByCategory(category));
    }

    // Nawaf - Endpoint returns a list of products with price more than or equal than the provided amount
    @GetMapping("/get/by-price/more-than-equal/{price}")
    public ResponseEntity getProductByPriceMoreThanOrEqual(@PathVariable Double price) {
        return ResponseEntity.status(200).body(productService.getProductByPriceMoreThanOrEqual(price));
    }

    // Nawaf - Endpoint returns a list of products with price less than or equal than the provided amount
    @GetMapping("/get/by-price/less-than-equal/{price}")
    public ResponseEntity getProductByPriceLessThanOrEqual(@PathVariable Double price) {
        return ResponseEntity.status(200).body(productService.getProductByPriceLessThanOrEqual(price));
    }

    // Ebtehal - Endpoint returns a list of products with low quantity for a specific supplier, which is lower than 5
    @GetMapping("/low-quantity/{supplierId}")
    public ResponseEntity getLowQuantityProductsForSupplier(@PathVariable Integer supplierId) {
        List<ProductDTOout> productDTOs = productService.getLowQuantityProductsForSupplier(supplierId);
        return ResponseEntity.status(200).body(productDTOs);
    }

    // Nawaf - Endpoint lets the user to buy a quantity of products
    @PutMapping("/buy/{customerId}/{productId}/{quantity}")
    public ResponseEntity buyProductById(@PathVariable Integer customerId, @PathVariable Integer productId, @PathVariable Integer quantity) {
        productService.buyProductById(customerId, productId, quantity);
        return ResponseEntity.status(200).body(new ApiResponse("You have bought this product successfully"));
    }

    // Ebtehal - Endpoint lets the supplier to apply a discount for a specific product
    @PutMapping("/apply-discount/{supplierId}/{productId}/{discount}")
    public ResponseEntity applyDiscount(
            @PathVariable Integer supplierId,
            @PathVariable Integer productId,
            @PathVariable Double discount) {
        productService.applyDiscountToProduct(supplierId, productId, discount);
        return ResponseEntity.status(200).body(new ApiResponse("Discount has been applied for product with ID: " + productId));
    }
}
