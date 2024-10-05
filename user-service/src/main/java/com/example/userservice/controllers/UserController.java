package com.example.userservice.controllers;

import com.example.userservice.dtos.request.UserRequest;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.dtos.response.UserResponse;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.services.FileStorageService;
import com.example.userservice.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
@Tag(name = "User", description = "User Controller")
public class UserController {
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    ApiResponse<?> getAllUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get all users")
                .data(userService.getAll(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping("/count")
    ApiResponse<?> getCountUsers() {
        return ApiResponse.builder()
                .message("Get count users")
                .data(userService.countUsers())
                .build();
    }

    @GetMapping(path = "/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable(name = "id") Long id) {
        UserResponse user = userService.findById(id);
        return ApiResponse.<UserResponse>builder()
                .message("Get user by Id")
                .data(user)
                .build();
    }

    @GetMapping(path = "/role/{roleId}")
    ApiResponse<Page<UserResponse>> getUserByRoleId(@RequestParam(name = "page") int page,
                                                    @RequestParam(name = "limit") int limit,
                                                    @PathVariable(name = "roleId") Long roleId) {
        return ApiResponse.<Page<UserResponse>>builder()
                .message("Get user by Role Id")
                .data(userService.findByRoleId(roleId, PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/trash")
    ApiResponse<?> getInTrashUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get in trash users")
                .data(userService.getInTrash(PageRequest.of(page - 1, limit, Sort.by("createdAt").descending())))
                .build();
    }

    @GetMapping(path = "/username")
    ApiResponse<UserResponse> getUserByUsername(@RequestParam(name = "username") String username) {
        UserResponse user = userService.findByUsername(username);
        return ApiResponse.<UserResponse>builder()
                .message("Get user by Username")
                .data(user)
                .build();
    }

    @GetMapping(path = "/email")
    ApiResponse<UserResponse> getUserByEmail(@RequestParam(name = "email") String email) {
        return ApiResponse.<UserResponse>builder()
                .message("Get user by email")
                .data(userService.findByEmail(email))
                .build();
    }

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse newUser = userService.createUser(userRequest);
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create user")
                .data(newUser)
                .build();
    }

    @PutMapping(path = "/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ApiResponse.<UserResponse>builder()
                .message("Update user")
                .data(updatedUser)
                .build();
    }

    @DeleteMapping(path = "/{id}")
    ApiResponse<?> deleteUser(@PathVariable(name = "id") Long id) {
        userService.moveToTrash(id);
        return ApiResponse.builder().message("Delete user successfully").build();
    }

    @PostMapping(value = "/images",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> uploadImage(@RequestParam("file") MultipartFile imageFile){
        var dto = fileStorageService.storeUserImageFile(imageFile);

        return ApiResponse.builder().code(201).data(dto).build();
    }

    @GetMapping("/images/{filename:.+}")
    ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStorageService.loadUserImageFileAsResource(filename);

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

    @DeleteMapping ("/images/{filename:.+}")
    ResponseEntity<?> deleteFile(@PathVariable String filename){
        fileStorageService.deleteUserImageFile(filename);
        return ResponseEntity.ok("Delete user images successfully");
    }

    @PutMapping("/restore/{id}")
    ApiResponse<?> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return ApiResponse.builder()
                .message("Restore user successfully")
                .build();
    }

    @GetMapping("/search-by-specification")
    public ApiResponse<?> advanceSearchBySpecification(@RequestParam(defaultValue = "1", name = "page") int page,
                                                       @RequestParam(defaultValue = "10", name = "limit") int limit,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false) String[] user,
                                                       @RequestParam(required = false) String role) {
        return ApiResponse.builder()
                .message("List of Users")
                .data(userService.searchBySpecification(PageRequest.of(page -1, limit), sort, user, role))
                .build();
    }
}