package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.RefundReturnItemRequest;
import com.example.orderservice.dto.request.ReturnItemRequest;
import com.example.orderservice.dto.response.ReturnItemResponse;
import com.example.orderservice.entities.ReturnItem;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReturnItemMapper {
    ReturnItem ReturnItemRequesttoReturnItem(ReturnItemRequest request);
    @Mapping(source = "images", target = "images", qualifiedByName = "splitImages")
    ReturnItemResponse toReturnItemResponse(ReturnItem returnItem);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReturnItem(@MappingTarget ReturnItem returnItem, RefundReturnItemRequest request);
    @Mapping(target = "images", ignore = true)
    ReturnItem returnItemResponsetoReturnItem(ReturnItemResponse response);
    List<ReturnItemResponse> returnItemListToReturnItemResponseList(List<ReturnItem> returnItems);

    @Named("splitImages")
    default List<String> splitImages(String images) {
        return images != null ? Arrays.asList(images.split(",")) : null;
    }
}
