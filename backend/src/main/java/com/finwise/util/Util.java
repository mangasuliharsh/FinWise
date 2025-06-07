package com.finwise.util;

import com.finwise.entity.User;
import com.finwise.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Util {

    private final UserService userService;

    public Util(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the currently authenticated user from the security context
     * @return Optional containing the User if authenticated, empty otherwise
     */
    public Optional<User> getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is properly authenticated
        if (!isValidAuthentication(authentication)) {
            System.out.println("Util class: No valid authentication found");
            return Optional.empty();
        }

        String email = authentication.getName();
        System.out.println("Util class - authenticated email: " + email);

        try {
            return userService.findByEmail(email);
        } catch (Exception e) {
            System.err.println("Error finding user by email in Util class: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Gets the email of the currently authenticated user
     * @return email string if authenticated, null otherwise
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isValidAuthentication(authentication)) {
            return null;
        }

        return authentication.getName();
    }

    /**
     * Checks if a user is currently authenticated
     * @return true if authenticated, false otherwise
     */
    public boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isValidAuthentication(authentication);
    }

    /**
     * Gets the current user's ID if authenticated
     * @return Optional containing the user ID if authenticated, empty otherwise
     */
    public Optional<Long> getCurrentUserId() {
        return getCurrentAuthenticatedUser().map(User::getId);
    }

    /**
     * Validates if the authentication is valid and not anonymous
     * @param authentication the Authentication object to validate
     * @return true if valid and authenticated, false otherwise
     */
    private boolean isValidAuthentication(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)
                && !authentication.getName().equals("anonymousUser");
    }

    /**
     * Debug method to print current authentication state
     */
    public void debugAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("=== Authentication Debug ===");
            System.out.println("Auth Type: " + authentication.getClass().getSimpleName());
            System.out.println("Principal: " + authentication.getPrincipal());
            System.out.println("Name: " + authentication.getName());
            System.out.println("Is Authenticated: " + authentication.isAuthenticated());
            System.out.println("Authorities: " + authentication.getAuthorities());
            System.out.println("Is Anonymous: " + (authentication instanceof AnonymousAuthenticationToken));
            System.out.println("===========================");
        } else {
            System.out.println("Authentication is null");
        }
    }
}
