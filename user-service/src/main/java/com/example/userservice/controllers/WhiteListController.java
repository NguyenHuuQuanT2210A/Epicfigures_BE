package com.example.userservice.controllers;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;
import com.example.userservice.services.WhiteListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/white_list")
@Tag(name = "White List", description = "White List Controller")
public class WhiteListController {
    private final WhiteListService whiteListService;

    @GetMapping
    ApiResponse<List<WhiteList>> getAll() {
        return ApiResponse.<List<WhiteList>>builder()
                .message("Get all white lists")
                .data(whiteListService.getAllWhiteList())
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<WhiteList>> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<WhiteList>>builder()
                .message("Get white lists by user id")
                .data(whiteListService.getWhiteListByUserId(userId))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<Long> getByProductId(@PathVariable Long productId) {
        return ApiResponse.<Long>builder()
                .message("Get white lists by product id")
                .data(whiteListService.getWhiteListByProductId(productId))
                .build();
    }

    @PostMapping
    ApiResponse<String> createWhiteList(@RequestBody UserAndProductId ids) {
        return ApiResponse.<String>builder()
                .code(HttpStatus.CREATED.value())
                .message("Created white list")
                .data(whiteListService.addWhiteList(ids))
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteById(@RequestBody UserAndProductId ids) {
        whiteListService.deleteWhiteList(ids);
        return ApiResponse.<String>builder()
                .message("Delete WhiteList Successfully")
                .build();
    }
}
