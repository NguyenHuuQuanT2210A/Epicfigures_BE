package com.example.userservice.controllers;

import com.example.userservice.dtos.request.BlogRequest;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.BlogResponse;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.services.BlogService;
import com.example.userservice.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class BlogController {
    private final BlogService blogService;
    private final FileStorageService fileStorageService;

    @GetMapping("/getAll")
    ApiResponse<Page<BlogResponse>> getAllBlogs(@RequestParam(defaultValue = "1", name = "page") int page,
                                                @RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ApiResponse.<Page<BlogResponse>>builder()
                .message("Get all Blogs")
                .data(blogService.getAllBlogs(PageRequest.of(page - 1, limit)))
                .build();
    }

    @GetMapping("/id/{id}")
    ApiResponse<BlogResponse> getBlogById(@PathVariable Long id) {
        return ApiResponse.<BlogResponse>builder()
                .message("Get Blog by Id")
                .data(blogService.getBlogById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ApiResponse.<Void>builder()
                .message("Delete Blog Successfully")
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> saveBlog(@RequestPart("blog") BlogRequest request, @RequestParam("file") MultipartFile imageFile, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build();
        }
        return ApiResponse.<BlogResponse>builder()
                .message("Create a new Blog")
                .data(blogService.addBlog(request, imageFile))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<?> updateBlog(@PathVariable Long id, @RequestPart("blog")BlogRequest request, @RequestParam("file") MultipartFile imageFile, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(fieldError -> fieldError.getField(), fieldError -> fieldError.getDefaultMessage()));
            return ApiResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Error")
                    .data(errors)
                    .build();
        }
        return ApiResponse.<BlogResponse>builder()
                .message("Update Blog")
                .data(blogService.updateBlog(id, request, imageFile))
                .build();
    }

    @GetMapping("/blog/{filename:.+}")
    ResponseEntity<?> getFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadBlogImageFileAsResource(filename);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception ex){
            throw new CustomException("File not found" + ex, HttpStatus.NOT_FOUND);
        }
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }
}
