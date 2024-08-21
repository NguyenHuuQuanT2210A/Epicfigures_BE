//package com.example.inventoryservice.entities.seeder;
//
//import com.example.productservice.statics.enums.ProductSimpleStatus;
//import com.example.productservice.util.StringHelper;
//import com.github.javafaker.Faker;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class ProductSeeder implements CommandLineRunner {
//    ProductRepository productRepository;
//    CategoryRepository categoryRepository;
//    ProductImageRepository productImageRepository;
//    Faker faker = new Faker();
//
//    public ProductSeeder(
//                       ProductRepository productRepository,
//                       CategoryRepository categoryRepository,
//                       ProductImageRepository productImageRepository) {
//        this.productRepository = productRepository;
//        this.categoryRepository = categoryRepository;
//        this.productImageRepository = productImageRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
////        //check if any value in db, if not, seed data
////        if (orderRepository.count() > 0) {
////            return;
////        }
//        createProducts();
////        createProducts();
////        createOrders();
//        //seeding demo cart data
////        ShoppingCart shoppingCart = new ShoppingCart();
////        shoppingCart.setUser(userRepository.getById(1L));
////        Set<CartItem> cartItems = new LinkedHashSet<>();
////        CartItem cartItem = new CartItem();
////        cartItem.setProduct(productRepository.getById(1L));
////        cartItem.setQuantity(2);
////        cartItem.setTotal(BigDecimal.valueOf(productRepository.findById(1L).get().getPrice()));
////        cartItem.setShoppingCart(shoppingCart);
////        cartItems.add(cartItem);
////        shoppingCart.setCartItems(cartItems);
////        BigDecimal total = new BigDecimal(0);
////        for (CartItem c:
////             cartItems) {
////            total = total.add(c.getTotal());
////        }
////        shoppingCart.setTotal(total);
////        shoppingCartRepository.save(shoppingCart);
////        cartItemRepository.save(cartItem);
//    }
//
//    private void createProducts() {
//        List<ProductImage> productImages = new ArrayList<>();
//
//        List<Category> categories = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            String categoryName = faker.food().ingredient();
//            Category category = new Category();
//            category.setCategoryName(categoryName);
//            categories.add(category);
//        }
//        categoryRepository.saveAll(categories);
//
//        boolean nameExisting = false;
//        List<Product> products = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            String productName = faker.food().dish();
//
//            for (Product product :
//                    products) {
//                if (product.getName().equals(productName)) {
//                    nameExisting = true;
//                    break;
//                }
//            }
//            if (nameExisting) {
//                i--;
//                nameExisting = false;
//                continue;
//            }
//            String slug = StringHelper.toSlug(productName);
//            String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
//            double price = (faker.number().randomNumber(5, true));
//            ProductSimpleStatus status = ProductSimpleStatus.ACTIVE;
//            Product product = new Product();
//            product.setName(productName);
//            product.setCategory(categories.get(faker.number().numberBetween(0, 6)));
//            product.setStockQuantity(faker.number().numberBetween(1, 100));
////            product.setSlug(slug);
//            product.setDescription(description);
////            product.setThumbnails("demo-img.jpg");
//            product.setPrice(BigDecimal.valueOf(price));
////            product.setStatus(status);
//            products.add(product);
//        }
//        productRepository.saveAll(products);
//
//        for (int j = 0; j < 30; j++) {
//            ProductImage productImage = new ProductImage();
//            productImage.setProduct(products.get(faker.number().numberBetween(0, 19)));
//            productImage.setImageUrl("demo-img.jpg");
//            productImages.add(productImage);
//        }
//
//        productImageRepository.saveAll(productImages);
//    }
//
//}
