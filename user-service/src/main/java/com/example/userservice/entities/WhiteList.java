package com.example.userservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "white_list")
public class WhiteList {
    @EmbeddedId
    private UserAndProductId id = new UserAndProductId();
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User user;
}
