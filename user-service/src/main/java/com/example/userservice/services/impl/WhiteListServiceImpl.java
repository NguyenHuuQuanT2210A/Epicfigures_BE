package com.example.userservice.services.impl;

import com.example.userservice.entities.UserAndProductId;
import com.example.userservice.entities.WhiteList;
import com.example.userservice.repositories.WhiteListRepository;
import com.example.userservice.services.WhiteListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhiteListServiceImpl implements WhiteListService {
    private final WhiteListRepository whiteListRepository;

    @Override
    public List<WhiteList> getAllWhiteList() {
        return whiteListRepository.findAll().stream().toList();
    }

    @Override
    public Optional<WhiteList> getWhiteListById(UserAndProductId ids) {
        return whiteListRepository.findById(ids);
    }

    @Override
    public List<WhiteList> getWhiteListByUserId(Long userId) {
        return whiteListRepository.findAllByUserId(userId);
    }

    @Override
    public Long getWhiteListByProductId(Long productId) {
        return whiteListRepository.findAllByProductId(productId).stream().count();
    }

    @Override
    public String addWhiteList(UserAndProductId ids) {
        var whiteList = getWhiteListById(ids);
        if (whiteList.isPresent()) {
            deleteWhiteList(ids);
            return "Delete White List Successfully!";
        }
        whiteListRepository.save(WhiteList.builder().id(ids).build());
        return "Add White List Successfully!";
    }

    @Override
    public void deleteWhiteList(UserAndProductId ids) {
        whiteListRepository.deleteById(ids);
    }
}
