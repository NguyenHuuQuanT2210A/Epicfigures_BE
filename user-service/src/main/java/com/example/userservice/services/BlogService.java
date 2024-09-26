package com.example.userservice.services;

import com.example.userservice.dtos.request.BlogRequest;
import com.example.userservice.dtos.response.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService {
    Page<BlogResponse> getAllBlogs(Pageable pageable);
    BlogResponse getBlogById(Long id);
    BlogResponse updateBlog(Long id, BlogRequest inventoryRequest, MultipartFile imageFile);
    BlogResponse addBlog(BlogRequest inventoryRequest, MultipartFile imageFile);
    void deleteBlog(Long id);
    void moveToTrash(Long id);
    Page<BlogResponse> getInTrash(Pageable pageable);
    void restoreBlog(Long id);
}
