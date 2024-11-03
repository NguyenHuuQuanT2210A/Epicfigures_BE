package com.example.orderservice.controller;

import com.example.orderservice.dto.request.RefundReturnItemRequest;
import com.example.orderservice.dto.request.ReturnItemRequest;
import com.example.orderservice.dto.request.ReturnItemStatusRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.ReturnItemResponse;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.service.ReturnItemService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Valid
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/return_item")
public class ReturnItemController {
    private final ReturnItemService returnItemService;

    @GetMapping("/search-by-specification")
    ApiResponse<?> advanceSearchBySpecification(@RequestParam(defaultValue = "1", name = "page") int page,
                                                @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(required = false) String[] returnItem,
                                                @RequestParam(required = false) String[] orderDetail) {
        return ApiResponse.builder()
                .message("List of ReturnItems")
                .data(returnItemService.searchBySpecification(PageRequest.of(page -1, limit), sort, returnItem, orderDetail))
                .build();
    }

    @GetMapping("/getAll")
    ApiResponse<Page<ReturnItemResponse>> getAllReturnItems(
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<ReturnItemResponse>>builder()
                .message("Get all ReturnItems")
                .data(returnItemService.getAllReturnItems(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<?> getReturnItemById(@PathVariable Long id) {
        ReturnItemResponse returnItem = returnItemService.getReturnItemById(id);
        if (returnItem == null) {
            throw new NotFoundException("ReturnItem not found with id: " + id);
        }
        return ApiResponse.builder()
                .message("Get returnItem by Id")
                .data(returnItem)
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<?> getReturnItemsByUserId(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "1", name = "page") int page,
                                          @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get returnItem by Id")
                .data(returnItemService.getReturnItemByUserId(userId, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> addReturnItem(@Valid @RequestPart("returnItemRequest") ReturnItemRequest request, @RequestPart("files") @NonNull List<MultipartFile> imageFiles, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Create returnItem successfully")
                .data(returnItemService.addReturnItem(request, imageFiles))
                .build());
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateReturnItem(@PathVariable Long id, @RequestBody RefundReturnItemRequest request) {
        returnItemService.updateReturnItem(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Update returnItem successfully")
                .build());
    }

    @PutMapping("/status/{id}")
    ResponseEntity<?> updateStatusReturnItem(@PathVariable Long id, @RequestBody ReturnItemStatusRequest returnItemStatusRequest) {
        returnItemService.updateStatusReturnItem(id, returnItemStatusRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Update returnItem successfully")
                .build());
    }

    @DeleteMapping("/in-trash/{id}")
    ApiResponse<?> moveToTrash(@PathVariable Long id) {
        returnItemService.moveToTrash(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Move to trash returnItem successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteReturnItem(@PathVariable Long id) {
        returnItemService.deleteReturnItem(id);
        return ApiResponse.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete ReturnItem Successfully")
                .build();
    }

    @GetMapping("/trash")
    ApiResponse<?> getInTrashReturnItem(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "limit", defaultValue = "10") int limit){
        return ApiResponse.builder()
                .message("Get in trash ReturnItem")
                .data(returnItemService.getInTrash(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreReturnItem(@PathVariable Long id) {
        returnItemService.restoreReturnItem(id);
        return ApiResponse.builder()
                .message("Restore returnItem successfully")
                .build();
    }
}
