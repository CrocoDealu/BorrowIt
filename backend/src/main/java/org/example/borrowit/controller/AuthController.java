package org.example.borrowit.controller;

import org.example.borrowit.domain.User;
import org.example.borrowit.dto.LoginResponse;
import org.example.borrowit.dto.RegisterRequest;
import org.example.borrowit.dto.UserDto;
import org.example.borrowit.service.UserService;
import org.example.borrowit.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optUser = userService.getUserByEmail(loginRequest.getEmail());
        if (optUser.isPresent()) {
            if (optUser.get().getPassword().equals(loginRequest.getPassword())) {
                User user = optUser.get();
                String token = "token" + user.getId();
                userService.addUserSession(token, user);
                UserDto userDto = user.toDto();
                LoginResponse loginResponse = new LoginResponse(token, userDto);
                return ResponseEntity.ok(loginResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Incorrect password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Non existent email"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Optional<User> newUser = registerUser(registerRequest);

        if (newUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration succesful!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed!");
        }
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user != null) {
            return ResponseEntity.ok(Collections.singletonMap("valid", true));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("valid", false));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.removeUserSession(token);
        return ResponseEntity.ok(Collections.singletonMap("message", "Logged out successfully"));
    }

    private Optional<User> registerUser(RegisterRequest registerRequest) {
        Optional<User> optUser = userService.getUserByEmail(registerRequest.getEmail());
        if (optUser.isPresent()) {
            return Optional.empty();
        }
        User user = new User(-1, registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getAddress(), registerRequest.getContactNumber(), 0);
        user = userService.registerUser(user);
        return Optional.of(user);
    }
}
