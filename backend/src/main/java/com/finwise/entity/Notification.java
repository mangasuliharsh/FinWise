package com.finwise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;
    private boolean isRead = false;
    private String notificationType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

