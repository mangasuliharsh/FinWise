package com.finwise.service;

import com.finwise.dto.UserDTO;
import com.finwise.entity.User;
import com.finwise.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    ModelMapper modelMapper = new ModelMapper();
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User oAuth2User) {
            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                // Log this for debugging
                System.out.println("OAuth2User does not contain an email attribute.");
                return null;
            }
            return userRepository.findByEmail(email);
        }

        return null;
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDTO saveUser(User user) {
         User savedUser = modelMapper.map(user,User.class);
         return modelMapper.map(userRepository.save(savedUser),UserDTO.class);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserIdByEmailOrUsername(String email,String username) {
        return userRepository.findByEmailOrUsername(email,username);
    }
}
