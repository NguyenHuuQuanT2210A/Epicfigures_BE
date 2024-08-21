package com.example.userservice.services;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;

import java.util.List;

public interface WhiteListService {
    List<WhiteList> getAllWhiteList();
    List<WhiteList> getWhiteListByUserId(Long userId);
    List<WhiteList> getWhiteListByProductId(Long productId);
    WhiteList addWhiteList(UserAndProductId ids);
    void deleteWhiteList(UserAndProductId ids);
}
