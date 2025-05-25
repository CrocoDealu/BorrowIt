package org.example.borrowit;

import org.example.borrowit.domain.User;
import org.example.borrowit.repository.UserDbRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BorrowItApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowItApplication.class, args);
    }
}
