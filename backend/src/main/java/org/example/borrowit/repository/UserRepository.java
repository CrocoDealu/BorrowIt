package org.example.borrowit.repository;

import org.example.borrowit.domain.User;

import java.util.Optional;

public interface UserRepository extends IRepository<Integer, User> {
    public Optional<User> findByEmail(String email);
}
