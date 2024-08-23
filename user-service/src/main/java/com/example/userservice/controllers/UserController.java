package com.example.userservice.controllers;

import com.example.userservice.dtos.UserDTO;
import com.example.userservice.dtos.response.ApiResponse;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.models.requests.UserRequest;
import com.example.userservice.services.FileStorageService;
import com.example.userservice.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User Controller")
@CrossOrigin
@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FileStorageService fileStorageService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    ApiResponse<?> getAllUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get all users")
                .data(userService.getAll(PageRequest.of(page - 1, limit)))
                .build();
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/{id}")
    ApiResponse<UserDTO> getUserById(@PathVariable(name = "id") Long id) {
        UserDTO user = userService.findById(id);
        return ApiResponse.<UserDTO>builder()
                .message("Get user by Id")
                .data(user)
                .build();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/trash")
    ApiResponse<?> getInTrashUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
        return ApiResponse.builder()
                .message("Get in trash users")
                .data(userService.getInTrash(PageRequest.of(page - 1, limit)))
                .build();
    }

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/username")
    ApiResponse<UserDTO> getUserByUsername(@RequestParam(name = "username") String username) {
        UserDTO user = userService.findByUsername(username);
        return ApiResponse.<UserDTO>builder()
                .message("Get user by Username")
                .data(user)
                .build();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    ApiResponse<UserDTO> createUser(@RequestBody UserRequest userRequest) {
        UserDTO newUser = userService.createUser(userRequest);
        return ApiResponse.<UserDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create user")
                .data(newUser)
                .build();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/{id}")
    ApiResponse<UserDTO> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserRequest userRequest) {
        UserDTO updatedUser = userService.updateUser(id, userRequest);
        return ApiResponse.<UserDTO>builder()
                .message("Update user")
                .data(updatedUser)
                .build();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping ("/images/{filename:.+}")
    ResponseEntity<?> deleteFile(@PathVariable String filename){
        fileStorageService.deleteUserImageFile(filename);
        return ResponseEntity.ok("Delete user images successfully");
    }
}