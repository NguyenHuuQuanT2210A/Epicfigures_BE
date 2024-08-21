package com.example.productservice.security.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    private Set<String> blacklist = new HashSet<>();

    public void invalidateToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return blacklist.contains(token);
    }
}