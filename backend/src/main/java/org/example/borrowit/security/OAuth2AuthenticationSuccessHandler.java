package org.example.borrowit.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.borrowit.domain.User;
import org.example.borrowit.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public OAuth2AuthenticationSuccessHandler(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        setDefaultTargetUrl("http://localhost:5173/oauth2/redirect");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        if (email == null) {
            throw new IllegalArgumentException("Email not found from OAuth2 provider");
        }

        Optional<User> existingUser = userService.getUserByEmail(email);

        User user;
        boolean isNewUser = false;

        if (existingUser.isEmpty()) {
            isNewUser = true;

            String name = (String) attributes.get("name");
            String firstName = (String) attributes.getOrDefault("given_name", "");
            String lastName = (String) attributes.getOrDefault("family_name", "");
            String pictureUrl = (String) attributes.getOrDefault("picture", "");

            user = createUserFromOAuth2(email, name, firstName, lastName, pictureUrl, provider);
        } else {
            user = existingUser.get();
        }

        String token = jwtTokenUtil.generateToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .queryParam("newUser", isNewUser)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User createUserFromOAuth2(String email, String name, String firstName,
                                      String lastName, String pictureUrl, String provider) {
        String randomPassword = UUID.randomUUID().toString();

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(randomPassword);
        return userService.registerUser(newUser);
    }
}
