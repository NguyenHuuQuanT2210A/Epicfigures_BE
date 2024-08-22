package com.example.userservice.services.impl;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;
import com.example.userservice.repositories.WhiteListRepository;
import com.example.userservice.services.WhiteListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhiteListServiceImpl implements WhiteListService {
    private final WhiteListRepository whiteListRepository;

    @Override
    public List<WhiteList> getAllWhiteList() {
        return whiteListRepository.findAll().stream().toList();
    }

    @Override
    public List<WhiteList> getWhiteListByUserId(Long userId) {
        return whiteListRepository.findAllByUserId(userId);
    }

    @Override
    public List<WhiteList> getWhiteListByProductId(Long productId) {
        return whiteListRepository.findAllByProductId(productId);
    }

    @Override
    public WhiteList addWhiteList(UserAndProductId ids) {
        return whiteListRepository.save(WhiteList.builder().id(ids).build());
    }

    @Override
    public void deleteWhiteList(UserAndProductId ids) {
        whiteListRepository.deleteById(ids);
    }
}
