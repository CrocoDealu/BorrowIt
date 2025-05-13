package org.example.borrowit.repository;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbRepository implements UserRepository {

    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(UserDbRepository.class);
    public UserDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        logger.info("Finding user by email: " + email);
        String hql = "FROM User u WHERE u.email = :email";
        Optional<User> optUser;
        try (Session session = getSession()) {
            Query query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            try {
                User user = (User) query.getSingleResult();
                optUser = Optional.of(user);
            } catch (NoResultException e) {
                logger.warn("No user found for email: " + email);
                optUser = Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by email: " + email + " " + e.getMessage());
        }
        logger.info("User found: " + optUser);
        return optUser;
    }

    @Override
    public Iterable<User> findAll() {
        logger.info("Finding all users");
        String hql = "FROM User";
        List<User> users;
        try (Session session = getSession()) {
            Query query = session.createQuery(hql, User.class);
            users = (List<User>) query.getResultList();
        } catch (NoResultException e) {
            logger.warn("No users found");
            users = new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all users: " + e.getMessage());
        }
        logger.info("Users found: " + users);
        return users;
    }

    @Override
    public Optional<User> findById(Integer integer) {
        logger.info("Finding user by id: " + integer);
        try (Session session = getSession()) {
            User user = session.find(User.class, integer);
            logger.info("Found the user by id: " + integer);
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            logger.warn("No user found for id: {}", integer);
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by id: " + integer + " " + e.getMessage());
        }
    }

    @Override
    public User save(User entity) {
        logger.info("Saving user: " + entity);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving user: " + entity + " " + e.getMessage());
        }
        logger.info("User saved: " + entity);
        return entity;
    }

    @Override
    public Optional<User> deleteById(Integer integer) {
        logger.info("Deleting user by id: " + integer);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, integer);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                logger.info("User deleted: {}", integer);
                return Optional.of(user);
            } else {
                transaction.rollback();
                logger.warn("Error finding user: {}", integer);
                return Optional.empty();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting user by id: " + integer + " " + e.getMessage());
        }
    }

    @Override
    public User update(User detachedUser) {
        logger.info("Updating user: " + detachedUser);
        Transaction transaction = null;
        User updatedUser = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            updatedUser = session.merge(detachedUser);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
             throw new RuntimeException("Error updating user: " + detachedUser + " " + e.getMessage());
        }
        logger.info("User updated: " + updatedUser);
        return updatedUser;
    }
}
