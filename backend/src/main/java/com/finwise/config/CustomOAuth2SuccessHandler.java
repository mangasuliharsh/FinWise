package com.finwise.config;

import com.finwise.entity.User;
import com.finwise.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.redirect-uri}")
    private String redirectUri;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");
        String picture = oauthUser.getAttribute("picture");

        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setUpdatedAt(LocalDateTime.now());
            user.setProfileImageUrl(picture);
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setFirstName(firstName != null ? firstName : "OAuth2User");
            user.setLastName(lastName != null ? lastName : "");
            user.setProfileImageUrl(picture);
            user.setPassword("");
            user.setRole("USER");
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setCreatedAt(LocalDateTime.now());
        }

        userRepository.save(user);

        String jwt = jwtService.generateToken(email);
        response.sendRedirect(redirectUri + "?token=" + jwt);
    }
}
