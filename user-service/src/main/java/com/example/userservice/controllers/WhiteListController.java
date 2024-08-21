package com.example.userservice.controllers;

import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.entities.Cart;
import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;
import com.example.userservice.services.WhiteListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "White List", description = "White List Controller")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "api/v1/white_list")
public class WhiteListController {
    WhiteListService whiteListService;

    @GetMapping
    ApiResponse<List<WhiteList>> getAll() {
        return ApiResponse.<List<WhiteList>>builder()
                .message("Get all white lists")
                .result(whiteListService.getAllWhiteList())
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<WhiteList>> getByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<WhiteList>>builder()
                .message("Get white lists by user id")
                .result(whiteListService.getWhiteListByUserId(userId))
                .build();
    }

    @GetMapping("/product/{productId}")
    ApiResponse<List<WhiteList>> getByProductId(@PathVariable Long productId) {
        return ApiResponse.<List<WhiteList>>builder()
                .message("Get white lists by product id")
                .result(whiteListService.getWhiteListByProductId(productId))
                .build();
    }

    @PostMapping
    ApiResponse<WhiteList> createWhiteList(@RequestBody UserAndProductId ids) {
        return ApiResponse.<WhiteList>builder()
                .message("Created white list")
                .result(whiteListService.addWhiteList(ids))
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteById(@RequestBody UserAndProductId ids) {
        whiteListService.deleteWhiteList(ids);
        return ApiResponse.<String>builder()
                .message("Deleted WhiteList")
                .build();
    }
}
