package com.example.userservice.mappers;


import com.example.userservice.dtos.request.BlogRequest;
import com.example.userservice.dtos.response.BlogResponse;
import com.example.userservice.entities.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BlogMapper {
//    @Mapping(source = "user", target = "user")
    BlogResponse toBlogResponse(Blog blog);

    Blog toBlog(BlogRequest request);
    void updatedBlog(@MappingTarget Blog blog, BlogRequest request);
}
