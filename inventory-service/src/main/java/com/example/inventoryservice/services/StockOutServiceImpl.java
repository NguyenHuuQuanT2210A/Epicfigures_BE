package com.example.inventoryservice.services;//package com.example.productservice.services;
//
//import com.example.inventoryservice.dtos.StockOutDTO;
//import com.example.productservice.entities.Clinic;
//import com.example.productservice.entities.Product;
//import com.example.productservice.entities.StockOut;
//import com.example.productservice.repositories.ClinicRepository;
//import com.example.productservice.repositories.ProductRepository;
//import com.example.productservice.repositories.StockOutRepository;
//import com.example.productservice.statics.enums.Reason;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class StockOutServiceImpl implements StockOutService{
//
//
//    private final StockOutRepository stockOutRepository;
//
//
//    private final StockOutMapper stockOutMapper;
//
//
//    private final ProductRepository productRepository;
//
//
//    private final ClinicRepository clinicRepository;
//
//    @Override
//    public List<StockOutDTO> getAllStockOutByProductId(long productId) {
//        Optional<Product> product = productRepository.findById(productId);
//        Product product1;
//        if(product.isPresent()){
//            product1 = product.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với product_id" + productId);
//        List<StockOut> stockOuts = stockOutRepository.findStockOutByProduct(product1);
//        return stockOuts.stream()
//                .map(stockOutMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<StockOutDTO> getAllStockOutByClinicId(long clinicId) {
//        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
//        Clinic clinic1;
//        if(clinic.isPresent()){
//            clinic1 = clinic.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với clinic_id" + clinicId);
//        List<StockOut> stockOuts = stockOutRepository.findStockOutByClinic(clinic1);
//        return stockOuts.stream()
//                .map(stockOutMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<StockOutDTO> getAllStockOutByProductIdAndClinicId(long productId, long clinicId) {
//        Optional<Product> product = productRepository.findById(productId);
//        Product product1;
//        if(product.isPresent()){
//            product1 = product.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với product_id" + productId);
//        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
//        Clinic clinic1;
//        if(clinic.isPresent()){
//            clinic1 = clinic.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với clinic_id" + clinicId);
//        List<StockOut> stockOuts = stockOutRepository.findStockOutByProductAndClinic(product1, clinic1);
//        return stockOuts.stream()
//                .map(stockOutMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void addStockOut(StockOutDTO stockOutDTO) {
//        StockOut stockOut = stockOutMapper.toEntity(stockOutDTO);
//        stockOutRepository.save(stockOut);
//    }
//
//    @Override
//    public void updateStockOut(long id, StockOutDTO stockOutDTO) {
//        Optional<StockOut> stockOut = stockOutRepository.findById(id);
//        if (stockOut.isPresent()) {
//            StockOut stockOut1 = stockOut.get();
//            if(stockOutDTO.getQuantity()!= 0){
//                stockOut1.setQuantity(stockOutDTO.getQuantity());
//            }
//            if(stockOutDTO.getReason()!= null){
//                stockOut1.setReason(Reason.valueOf(stockOutDTO.getReason()));
//            }
//            if(stockOutDTO.getDateOut()!= null){
//                stockOut1.setDateOut(stockOutDTO.getDateOut());
//            }
//            stockOutRepository.save(stockOut1);
//        } else {
//            throw new RuntimeException("Không tìm thấy StockOut với id" + id);
//        }
//    }
//
//    @Override
//    public void deleteStockOut(long id) {
//        Optional<StockOut> stockOut = stockOutRepository.findById(id);
//        StockOut stockOut1;
//        if(stockOut.isPresent()) {
//            stockOut1 = stockOut.get();
//        } else throw new RuntimeException("Không tìm thấy InStock với id" + id);
//        stockOutRepository.delete(stockOut1);
//    }
//}
