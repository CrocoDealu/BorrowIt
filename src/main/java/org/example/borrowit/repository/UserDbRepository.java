package org.example.borrowit.repository;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.example.borrowit.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbRepository implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String hql = "FROM User u WHERE u.email = :email";
        Optional<User> optUser;
        try (Session session = getSession()) {
            Query query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            try {
                User user = (User) query.getSingleResult();
                optUser = Optional.of(user);
            } catch (NoResultException e) {
                optUser = Optional.empty();
            }
        }

        return optUser;
    }

    @Override
    public Iterable<User> findAll() {
        String hql = "FROM User";
        List<User> users;
        try (Session session = getSession()) {
            Query query = session.createQuery(hql, User.class);
            users = (List<User>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
        return users;
    }

    @Override
    public Optional<User> findById(Integer integer) {
        try (Session session = getSession()) {
            return Optional.ofNullable(session.find(User.class, integer));
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public User save(User entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.save(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Optional<User> deleteById(Integer integer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, integer);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                return Optional.of(user);
            } else {
                transaction.rollback();
                return Optional.empty();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public User update(User detachedUser) {
        Transaction transaction = null;
        User updatedUser = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            updatedUser = (User) session.merge(detachedUser);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return updatedUser;
    }
}
