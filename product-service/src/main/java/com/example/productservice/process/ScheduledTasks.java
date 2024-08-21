package com.example.productservice.process;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTasks {
    private final ProductCacheUpdater productCacheUpdater;

    @Scheduled(fixedRate = 30000)
    public void updateProductCache() {
        productCacheUpdater.updateCache();
    }
}