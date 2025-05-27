package com.finwise.util;

import com.finwise.entity.User;
import com.finwise.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

        String email = null;
        String username = null;

        if (auth instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = (OAuth2User) auth.getPrincipal();
            email = oauthUser.getAttribute("email");
            username = oauthUser.getAttribute("username");// This works for Google
            System.out.println("OAuth2User email: " + email);
        } else {
            // Username-password login
            email = auth.getName();
            System.out.println("Standard login email: " + email);
        }

        if (email != null) {
            User user = userService.findUserIdByEmailOrUsername(email,username);
            return user != null ? user.getId() : null;
        }

        return null;
    }


}
