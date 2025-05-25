package com.finwise.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private boolean isRead;
    private String notificationType;
    private Long userId;
}
