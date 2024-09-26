package com.example.userservice.mappers;

import com.example.userservice.dtos.request.AddressOrderRequest;
import com.example.userservice.dtos.response.AddressOrderResponse;
import com.example.userservice.entities.AddressOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressOrderMapper {
    AddressOrderMapper INSTANCE = Mappers.getMapper(AddressOrderMapper.class);

    AddressOrder toAddressOrder(AddressOrderRequest request);

    AddressOrderResponse toAddressOrderResponse(AddressOrder addressOrder);
    void updateAddressOrder(@MappingTarget AddressOrder addressOrder, AddressOrderRequest request);
}