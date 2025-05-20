package org.example.borrowit.controller;

import org.example.borrowit.domain.User;
import org.example.borrowit.dto.LoginResponse;
import org.example.borrowit.service.UserService;
import org.example.borrowit.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
         Optional<User> optUser = userService.getUserByEmail(loginRequest.getEmail());
         if (optUser.isPresent()) {
            if (optUser.get().getPassword().equals(loginRequest.getPassword())) {
                User user = optUser.get();
                String token = "token" + user.getId();
                LoginResponse loginResponse = new LoginResponse(token, user);
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
}
