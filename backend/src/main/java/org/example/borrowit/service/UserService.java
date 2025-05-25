package org.example.borrowit.service;

import org.example.borrowit.domain.User;
import org.example.borrowit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Map<String, User> userSessions = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> deleteUser(Integer id) {
        return userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.update(user);
    }

    public void addUserSession(String token, User user) {
        userSessions.put(token, user);
    }

    public User getUserByToken(String token) {
        return userSessions.get(token);
    }

    public void removeUserSession(String token) {
        userSessions.remove(token);
    }
}
