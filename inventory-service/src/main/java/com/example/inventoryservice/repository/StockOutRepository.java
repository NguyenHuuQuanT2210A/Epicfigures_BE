package com.example.inventoryservice.repository;//package com.example.productservice.repositories;
//
//import com.example.productservice.entities.Clinic;
//import com.example.productservice.entities.Product;
//import com.example.productservice.entities.StockOut;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface StockOutRepository extends JpaRepository<StockOut, Long>, JpaSpecificationExecutor<StockOut>{
//    List<StockOut> findStockOutByClinic(Clinic clinic);
//    List<StockOut> findStockOutByProduct(Product product);
//    List<StockOut> findStockOutByProductAndClinic(Product product, Clinic clinic);
//}