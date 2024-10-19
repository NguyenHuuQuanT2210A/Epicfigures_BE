package com.example.inventoryservice.entities.seeder;

import com.example.inventoryservice.dto.request.ProductQuantityRequest;
import com.example.inventoryservice.entities.Inventory;
import com.example.inventoryservice.entities.InventoryStatus;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.InventoryStatusRepository;
import com.example.inventoryservice.services.ProductClients;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

//@Component
public class InventorySeeder implements CommandLineRunner {
    InventoryRepository inventoryRepository;
    InventoryStatusRepository inventoryStatusRepository;
    ProductClients productClients;

    InventorySeeder(InventoryRepository inventoryRepository, InventoryStatusRepository inventoryStatusRepository, ProductClients productClients) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryStatusRepository = inventoryStatusRepository;
        this.productClients = productClients;
    }

    Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {
        //check if any value in db, if not, seed data
        if (inventoryRepository.count() > 0 && inventoryStatusRepository.count() > 0) {
            return;
        }
        createInventories();
    }

    private void createInventories() {
        List<InventoryStatus> inventoryStatuses = new ArrayList<>();
        List<Inventory> inventories = new ArrayList<>();

        List<String> inventoryStatusNames = List.of("IN", "OUT");
        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        for (String inventoryStatusName : inventoryStatusNames) {
                InventoryStatus inventoryStatus = new InventoryStatus();
                inventoryStatus.setName(inventoryStatusName);
                inventoryStatus.setDescription(description);
                inventoryStatus.setAddAction(inventoryStatusName.equals("IN"));
                inventoryStatus.setSystemType(true);
                inventoryStatuses.add(inventoryStatus);
        }

        inventoryStatusRepository.saveAll(inventoryStatuses);

        for (int i = 0; i < 20; i++) {
            Inventory inventory = new com.example.inventoryservice.entities.Inventory();
            var prdResponse = productClients.getProductById(i+1L).getData();
            inventory.setProductId(prdResponse.getProductId());
            inventory.setQuantity(prdResponse.getStockQuantity());
//            inventory.setUnitPrice(prdResponse.getPurchasePrice());
//            inventory.setTotalCost(inventory.getUnitPrice().multiply(BigDecimal.valueOf(prdResponse.getStockQuantity())));
            inventory.setNote(faker.lorem().sentence());
            inventory.setDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            inventory.setInventoryStatus(inventoryStatuses.get(0));
            inventories.add(inventory);

        }

        inventoryRepository.saveAll(inventories);
    }

}
