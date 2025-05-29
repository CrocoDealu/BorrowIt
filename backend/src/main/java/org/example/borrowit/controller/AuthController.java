package org.example.borrowit.controller;

import org.apache.tomcat.util.http.parser.Authorization;
import org.example.borrowit.domain.User;
import org.example.borrowit.dto.LoginRequest;
import org.example.borrowit.dto.LoginResponse;
import org.example.borrowit.dto.RegisterRequest;
import org.example.borrowit.dto.UserDto;
import org.example.borrowit.security.JwtTokenUtil;
import org.example.borrowit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            Optional<User> userOptional = userService.getUserByEmail(loginRequest.getEmail());
            User user = userOptional.get();
            String token = jwtTokenUtil.generateToken(user);
            System.out.println("A generat token");
            UserDto userDto = user.toDto();
            LoginResponse loginResponse = new LoginResponse(token, userDto);

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Optional<User> existingUser = userService.getUserByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
        }

        User user = new User(
                -1,
                registerRequest.getName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getAddress(),
                registerRequest.getContactNumber(),
                0
        );

        User savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenUtil.getUsernameFromToken(token);
            return ResponseEntity.ok(Collections.singletonMap("valid", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("valid", false));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> handleOAuth2Redirect(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof org.springframework.security.core.userdetails.User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) principal;

            String email = userDetails.getUsername();
            Optional<User> userOptional = userService.getUserByEmail(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userOptional.get();
            UserDto userDto = user.toDto();

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid token"));
        }
    }
}