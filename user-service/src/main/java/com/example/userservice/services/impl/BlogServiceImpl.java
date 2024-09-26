package com.example.userservice.services.impl;


import com.example.userservice.dtos.request.BlogRequest;
import com.example.userservice.dtos.response.BlogResponse;
import com.example.userservice.entities.Blog;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.exceptions.NotFoundException;
import com.example.userservice.mappers.BlogMapper;
import com.example.userservice.repositories.BlogRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.BlogService;
import com.example.userservice.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Page<BlogResponse> getAllBlogs(Pageable pageable) {
        Page<Blog> inventories = blogRepository.findByDeletedAtIsNull(pageable);
        return inventories.map(blogMapper::toBlogResponse);
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        return blogMapper.toBlogResponse(findBlogById(id));
    }


    @Override
    public BlogResponse updateBlog(Long id, BlogRequest blogRequest, MultipartFile imageFile) {
        Blog blog = findBlogById(id);

        if (imageFile == null) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        blogMapper.updatedBlog(blog, blogRequest);

        if (!Objects.equals(imageFile.getOriginalFilename(), "")){
            fileStorageService.deleteBlogImageFile(blog.getImageTitle());
            blog.setImageTitle(fileStorageService.storeBlogImageFile(imageFile));
        }

        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }

    @Override
    public BlogResponse addBlog(BlogRequest blogRequest, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new CustomException("Image files are required", HttpStatus.BAD_REQUEST);
        }

        Blog blog = blogMapper.toBlog(blogRequest);
        blog.setUser(userRepository.findById(blogRequest.getUserId()).orElseThrow(() -> new NotFoundException("User not found")));
        blog.setImageTitle(fileStorageService.storeBlogImageFile(imageFile));

        return blogMapper.toBlogResponse(blogRepository.save(blog));
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    private Blog findBlogById(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
    }

    @Override
    public void moveToTrash(Long id) {
        Blog blog = findBlogById(id);

        LocalDateTime now = LocalDateTime.now();
        blog.setDeletedAt(now);
        blogRepository.save(blog);
    }

    @Override
    public Page<BlogResponse> getInTrash(Pageable pageable) {
        Page<Blog> inventories = blogRepository.findByDeletedAtIsNotNull(pageable);
        return inventories.map(blogMapper::toBlogResponse);
    }

    @Override
    public void restoreBlog(Long id) {
        Blog blog = findBlogById(id);
        blog.setDeletedAt(null);
        blogRepository.save(blog);
    }
}
