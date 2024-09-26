package com.example.userservice.services.impl;

import com.example.userservice.dtos.request.AddressOrderRequest;
import com.example.userservice.dtos.response.AddressOrderResponse;
import com.example.userservice.entities.AddressOrder;
import com.example.userservice.exceptions.CustomException;
import com.example.userservice.mappers.AddressOrderMapper;
import com.example.userservice.repositories.AddressOrderRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.AddressOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressOrderServiceImpl implements AddressOrderService {
    private final AddressOrderRepository addressOrderRepository;
    private final AddressOrderMapper addressOrderMapper;
    private final UserRepository userRepository;

    @Override
    public List<AddressOrderResponse> getAddressOrdersByUserId(Long userId) {
        return addressOrderRepository.findByUserId(userId).stream().map(addressOrderMapper::toAddressOrderResponse).toList();
    }

    @Override
    public AddressOrderResponse getAddressOrderById(Long id) {
        return addressOrderMapper.toAddressOrderResponse(findAddressOrderById(id));
    }

    @Override
    public AddressOrderResponse addAddressOrder(AddressOrderRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        var addressOrder = addressOrderMapper.toAddressOrder(request);
        if (request.getIsDefault().equals("true")){
            var addressOrders = addressOrderRepository.findByUserId(request.getUserId());
            for (var address : addressOrders){
                address.setIsDefault("false");
                addressOrderRepository.save(address);
            }
        }
        addressOrder.setUser(user);
        return addressOrderMapper.toAddressOrderResponse(addressOrderRepository.save(addressOrder));
    }

    @Override
    public AddressOrderResponse updateAddressOrder(Long id, AddressOrderRequest request) {
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        var addressOrder = findAddressOrderById(id);

        if (user.getId() != findAddressOrderById(id).getUser().getId()){
            throw new CustomException("User not match", HttpStatus.BAD_REQUEST);
        }

        addressOrderMapper.updateAddressOrder(addressOrder, request);
        if (request.getIsDefault().equals("true")){
            var addressOrders = addressOrderRepository.findByUserId(request.getUserId());
            for (var address : addressOrders){
                if (Objects.equals(address.getId(), id)){
                    continue;
                }
                address.setIsDefault("false");
                addressOrderRepository.save(address);
            }
        }
        return addressOrderMapper.toAddressOrderResponse(addressOrderRepository.save(addressOrder));
    }

    @Override
    public void deleteAddressOrder(Long id) {
        addressOrderRepository.deleteById(id);
    }

    private AddressOrder findAddressOrderById(Long id) {
        return addressOrderRepository.findById(id).orElseThrow(() -> new CustomException("Address order not found", HttpStatus.NOT_FOUND));
    }
}
