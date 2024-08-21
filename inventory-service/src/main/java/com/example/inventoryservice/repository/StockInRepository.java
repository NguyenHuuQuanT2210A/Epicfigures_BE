package com.example.inventoryservice.repository;//package com.example.productservice.repositories;
//
//
//import com.example.productservice.entities.Clinic;
//import com.example.productservice.entities.Product;
//import com.example.productservice.entities.StockIn;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface StockInRepository extends JpaRepository<StockIn, Long>, JpaSpecificationExecutor<StockIn>{
//    List<StockIn> findStockInByClinic(Clinic clinic);
//    List<StockIn> findStockInByProduct(Product product);
//    List<StockIn> findStockInByProductAndClinic(Product product, Clinic clinic);
//
//}