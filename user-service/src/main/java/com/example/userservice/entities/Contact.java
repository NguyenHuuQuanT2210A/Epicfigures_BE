package com.example.userservice.entities;

import com.example.userservice.entities.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact")
public class Contact extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_reply_id")
    private Contact contactReply;
    private String username;
    private String phoneNumber;
    private String email;
    private String note;
    private boolean isRead;
    private boolean isImportant;
    private boolean isSpam;
}
