package com.example.productservice.entities.seeder;

import com.example.productservice.entities.*;
import com.example.productservice.helper.LocalDatetimeConverter;
import com.example.productservice.repositories.*;
import com.example.productservice.services.FirebaseService;
import com.example.productservice.statics.enums.ProductSimpleStatus;
import com.example.productservice.util.GenerateUniqueCode;
import com.example.productservice.util.StringHelper;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

//@Component
public class ProductSeeder implements CommandLineRunner {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductImageRepository productImageRepository;
    FirebaseService firebaseService;

    Faker faker = new Faker();

    public ProductSeeder(
                       ProductRepository productRepository,
                       CategoryRepository categoryRepository,
                       ProductImageRepository productImageRepository,
                       FirebaseService firebaseService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
        this.firebaseService = firebaseService;
    }

    @Override
    public void run(String... args) throws Exception {
//        //check if any value in db, if not, seed data
//        if (orderRepository.count() > 0) {
//            return;
//        }
        createProducts();
    }

    private void createProducts() {

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String categoryName = faker.food().ingredient();
            Category category = new Category();
            category.setCategoryName(categoryName);
            categories.add(category);
        }
        categoryRepository.saveAll(categories);

        boolean nameExisting = false;
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String productName = faker.food().dish();

            for (Product product :
                    products) {
                if (product.getName().equals(productName)) {
                    nameExisting = true;
                    break;
                }
            }
            if (nameExisting) {
                i--;
                nameExisting = false;
                continue;
            }
            String slug = StringHelper.toSlug(productName);
            String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
//            double price = (faker.number().randomNumber(2, true));
            ProductSimpleStatus status = ProductSimpleStatus.ACTIVE;
            Product product = new Product();
            product.setName(productName);
            do {
                product.setCodeProduct(GenerateUniqueCode.generateProductCode());
            } while (productRepository.existsByCodeProductAndDeletedAtIsNull(product.getCodeProduct()));

            product.setCategory(categories.get(faker.number().numberBetween(0, 6)));
            product.setStockQuantity(faker.number().numberBetween(10, 200));
            product.setReservedQuantity(0);
            product.setSoldQuantity(0);

//            product.setSlug(slug);
            product.setDescription(description);
//            product.setThumbnails("demo-img.jpg");
            product.setPrice(BigDecimal.valueOf(faker.number().randomNumber(2, true)));
//            product.setPurchasePrice(BigDecimal.valueOf(faker.number().randomNumber(2, true)));
//            product.setListPrice(BigDecimal.valueOf(0));
//            product.setSellingPrice(BigDecimal.valueOf(0));

//            product.setStatus(status);
            product.setManufacturer(faker.company().name());
            product.setSize(faker.food().measurement());
            product.setWeight(faker.food().measurement());
            product.setReturnPeriodDays(faker.number().numberBetween(7, 15));

            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            products.add(product);
        }
        productRepository.saveAll(products);

        List<ProductImage> productImages = new ArrayList<>();

        List<String> imageUrls = firebaseService.getAllImages("product-image/");

        for (String imageUrl : imageUrls) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(products.get(faker.number().numberBetween(0, 19)));
            productImage.setImageUrl(imageUrl);
            productImages.add(productImage);
        }

        productImageRepository.saveAll(productImages);
    }

}
