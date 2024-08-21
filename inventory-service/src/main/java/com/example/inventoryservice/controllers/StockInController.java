//package com.example.productservice.controllers;
//
//import com.example.productservice.dto.StockInDTO;
//
//import com.example.productservice.exception.NotFoundException;
//import com.example.productservice.service.InStockService;
//import com.example.productservice.service.StockInService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Tag(name = "Stock in", description = "Stock in Controller")
//@CrossOrigin
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/stockIns")
//public class StockInController {
//
//    private final StockInService stockInService;
//
//    private final InStockService inStockService;
//
//    @GetMapping("/product/{id}")
//    public ResponseEntity<List<StockInDTO>> getAllStockInByProduct(@PathVariable long id) {
//        List<StockInDTO> stockInDTO = stockInService.getAllStockInByProductId(id);
//        if (stockInDTO == null) {
//            throw new NotFoundException("Stock not found with ProductId: " + id);
//        }
//        return ResponseEntity.ok(stockInDTO);
//    }
//
//    @GetMapping("/clinic/{id}")
//    public ResponseEntity<List<StockInDTO>> getAllStockInByClinic(@PathVariable long id) {
//        List<StockInDTO> stockInDTO = stockInService.getAllStockInByClinicId(id);
//        if (stockInDTO == null) {
//            throw new NotFoundException("Stock not found with ClinicId: " + id);
//        }
//        return ResponseEntity.ok(stockInDTO);
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<List<StockInDTO>> getAllStockInByProductAndClinic(@RequestParam long clinicId,
//                                                                              @RequestParam long productId) {
//        List<StockInDTO> stockInDTO = stockInService.getAllStockInByProductIdAndClinicId(productId,clinicId);
//        if (stockInDTO == null) {
//            throw new NotFoundException("Stock not found ");
//        }
//        return ResponseEntity.ok(stockInDTO);
//    }
//
//    @PostMapping("/")
//    public ResponseEntity<?> addStockIn(@Valid @RequestBody StockInDTO stockInDTO, BindingResult result) {
//        if (result.hasErrors()) {
//            Map<String, String> errors = result.getFieldErrors().stream()
//                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
//            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//        }
//        stockInService.addStockIn(stockInDTO);
//        inStockService.updateInStock(stockInDTO.getProduct_id(),stockInDTO.getClinic_id());
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateStockIn(@PathVariable long id,
//                                            @Valid @RequestBody StockInDTO stockInDTO, BindingResult result){
//        if (result.hasErrors()) {
//            Map<String, String> errors = result.getFieldErrors().stream()
//                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
//            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//        }
//        stockInService.updateStockIn(id,stockInDTO);
//        return new ResponseEntity<>(HttpStatus.UPGRADE_REQUIRED);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteStockIn(@PathVariable long id, BindingResult result) {
//        if (result.hasErrors()) {
//            Map<String, String> errors = result.getFieldErrors().stream()
//                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
//            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//        }
//        stockInService.deleteStockIn(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
