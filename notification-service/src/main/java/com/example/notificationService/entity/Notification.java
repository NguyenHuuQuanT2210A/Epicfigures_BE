package com.example.notificationService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String message;
    @ColumnDefault("0")
    private Boolean isSendAll;
    private String type;

    @ColumnDefault("0")
    private Boolean isRead;
    private String url;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
