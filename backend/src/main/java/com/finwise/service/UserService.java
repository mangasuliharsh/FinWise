package com.finwise.service;

import com.finwise.entity.User;
import com.finwise.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                // Log this for debugging
                System.out.println("OAuth2User does not contain an email attribute.");
                return Optional.empty();
            }
            return userRepository.findByEmail(email);
        }

        return Optional.empty();
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
