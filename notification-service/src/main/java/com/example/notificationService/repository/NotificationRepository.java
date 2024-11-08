package com.example.notificationService.repository;

import com.example.notificationService.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrIsSendAllOrderByCreatedAtDesc(Long userId, Boolean isSendAll);
}
