package com.example.userservice.services;

import com.example.userservice.dtos.request.AddressOrderRequest;
import com.example.userservice.dtos.response.AddressOrderResponse;

import java.util.List;

public interface AddressOrderService {
    List<AddressOrderResponse> getAddressOrdersByUserId(Long userId);
    AddressOrderResponse getAddressOrderById(Long id);
    AddressOrderResponse addAddressOrder(AddressOrderRequest request);
    AddressOrderResponse updateAddressOrder(Long id, AddressOrderRequest request);
    void deleteAddressOrder(Long id);
}
