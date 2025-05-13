package org.example.borrowit.utils;

import org.example.borrowit.repository.UserDbRepository;
import org.example.borrowit.repository.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.hibernate.cfg.Configuration;


@org.springframework.context.annotation.Configuration
public class AppConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    @Bean
    public UserRepository userRepository(SessionFactory sessionFactory) {
        return new UserDbRepository(sessionFactory);
    }
}
