package com.finwise.service;

import com.finwise.dto.NotificationDTO;
import com.finwise.entity.Notification;
import com.finwise.entity.User;
import com.finwise.repository.NotificationRepository;
import com.finwise.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;

    // Constructor injection
    public NotificationService(NotificationRepository notificationRepo, UserRepository userRepo, ModelMapper modelMapper) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    public List<NotificationDTO> getAllByUserId(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user.getNotifications()
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }

    public NotificationDTO createNotification(NotificationDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = modelMapper.map(dto, Notification.class);
        notification.setUsernoti(user);

        Notification savedNotification = notificationRepo.save(notification);
        return modelMapper.map(savedNotification, NotificationDTO.class);
    }

    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        Notification updatedNotification = notificationRepo.save(notification);

        return modelMapper.map(updatedNotification, NotificationDTO.class);
    }
}
