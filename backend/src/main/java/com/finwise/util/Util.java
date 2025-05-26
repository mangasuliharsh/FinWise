package com.finwise.util;

import com.finwise.entity.User;
import com.finwise.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Util {

    private final UserService userService;

    public Util(UserService userService) {
        this.userService = userService;
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        String email = auth.getName();
        if (email != null) {
            User user = userService.findByEmail(email);
            return user.getId();
        }
        return null;
    }
}
