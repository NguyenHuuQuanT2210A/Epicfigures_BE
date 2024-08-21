package com.example.inventoryservice.services;//package com.example.productservice.services;
//
//import com.example.inventoryservice.dtos.InStockDTO;
//import com.example.productservice.entities.*;
//import com.example.productservice.repositories.*;
//import com.example.productservice.statics.enums.InStockStatus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class InStockServiceImpl implements InStockService{
//
//
//    private final InStockRepository inStockRepository;
//
//
//    private final ProductRepository productRepository;
//
//
//    private final ClinicRepository clinicRepository;
//
//
//    private final StockInRepository stockInRepository;
//
//
//    private final StockOutRepository stockOutRepository;
//
//    @Override
//    public List<InStockDTO> getInStockByProductId(long productId) {
//        Optional<Product> product = productRepository.findById(productId);
//        Product product1;
//        if(product.isPresent()) {
//            product1 = product.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với product_id" + productId);
//        List<InStock> inStocks = inStockRepository.findInStockByProduct(product1);
//        return inStocks.stream()
//                .map(InStockMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<InStockDTO> getInStockByClinicId(long clinicId) {
//        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
//        Clinic clinic1;
//        if(clinic.isPresent()) {
//            clinic1 = clinic.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với clinic_id" + clinicId);
//        List<InStock> inStocks = inStockRepository.findInStockByClinic(clinic1);
//        return inStocks.stream()
//                .map(InStockMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<InStockDTO> getInStockByProducIdAndClinicId(long productId, long clinicId) {
//        Optional<Product> product = productRepository.findById(productId);
//        if(product.isEmpty()) {
//            throw new RuntimeException("Không tìm thấy InStock với product_id" + productId);
//        }
//        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
//        Clinic clinic1;
//        if(clinic.isPresent()) {
//            clinic1 = clinic.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với clinic_id" + clinicId);
//        List<InStock> inStocks = inStockRepository.findInStockByProductAndClinic(product.get(),clinic1);
//        return inStocks.stream()
//                .map(InStockMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void updateInStock(long productId, long clinicId) {
//        long quantityStockOut = 0;
//        long quantityStockIn = 0;
//        long quantityInStock ;
//        Optional<Product> product = productRepository.findById(productId);
//        if(product.isEmpty()) {
//            throw new RuntimeException("Không tìm thấy InStock với product_id" + productId);
//        }
//        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
//        if(clinic.isEmpty()) {
//            throw new RuntimeException("Không tìm thấy InStock với clinic_id" + clinicId);
//        }
//        List<InStock> inStocks = inStockRepository.findInStockByProductAndClinic(product.get(),clinic.get());
//        if(inStocks.isEmpty()){
//            addInStock(productId, clinicId);
//        }
//        List<StockOut> stockOut = stockOutRepository.findStockOutByProductAndClinic(product.get(),clinic.get());
//        if(!stockOut.isEmpty()){
//            for (StockOut stockOut1: stockOut) {
//                quantityStockOut += stockOut1.getQuantity();
//            }
//        }
//        List<StockIn> stockIn = stockInRepository.findStockInByProductAndClinic(product.get(),clinic.get());
//        if(!stockIn.isEmpty()){
//            for (StockIn stockIn1: stockIn) {
//                quantityStockIn += stockIn1.getQuantity();
//            }
//        }
//        List<InStock> inStock1 = inStockRepository.findInStockByProductAndClinic(product.get(),clinic.get());
//        InStock inStock = inStock1.get(0);
//        quantityInStock = quantityStockIn - quantityStockOut;
//        inStock.setStockQuantity(quantityInStock);
//        inStock.setLastUpdated(LocalDateTime.now());
//        inStock.setStockStatus(determineStockStatus(quantityInStock));
//        inStockRepository.save(inStock);
//    }
//
//    @Override
//    public InStockStatus determineStockStatus(long quantity) {
//        if (quantity <= 0) {
//            return InStockStatus.OUT_OF_STOCK;
//        } else if (quantity < 100) {
//            return InStockStatus.RUNNING_LOW;
//        } else {
//            return InStockStatus.IN_STOCK;
//        }
//    }
//
//    public void addInStock(long product_id, long clinic_id){
//
//        InStock inStock = new InStock();
//
//        inStock.setStockQuantity(0);
//        Optional<Product> product = productRepository.findById(product_id);
//        if(product.isEmpty()) {
//            throw new RuntimeException("Không tìm thấy InStock với product_id" + product_id);
//        }
//        inStock.setProduct(product.get());
//        Optional<Clinic> clinic = clinicRepository.findById(clinic_id);
//        if(clinic.isEmpty()) {
//            throw new RuntimeException("Không tìm thấy InStock với clinic_id" + clinic_id);
//        }
//        inStock.setClinic(clinic.get());
//        inStock.setLastUpdated(LocalDateTime.now());
//        inStockRepository.save(inStock);
//    }
//}