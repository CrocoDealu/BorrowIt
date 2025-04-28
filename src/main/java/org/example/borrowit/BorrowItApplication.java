package org.example.borrowit;

import org.example.borrowit.domain.User;
import org.example.borrowit.repository.UserDbRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BorrowItApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(BorrowItApplication.class, args);

        UserDbRepository userDbRepository = context.getBean(UserDbRepository.class);

        User user = new User(1, "John Doe", "john.doe@example.com", "password123",
                "123 Main St", "123-456-7890", 1000);
        userDbRepository.save(user);

        System.out.println("User saved successfully!");
    }

}
