package com.example.userservice.services;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;

import java.util.List;
import java.util.Optional;

public interface WhiteListService {
    List<WhiteList> getAllWhiteList();
    Optional<WhiteList> getWhiteListById(UserAndProductId ids);
    List<WhiteList> getWhiteListByUserId(Long userId);
    Long getWhiteListByProductId(Long productId);
    String addWhiteList(UserAndProductId ids);
    void deleteWhiteList(UserAndProductId ids);
}
