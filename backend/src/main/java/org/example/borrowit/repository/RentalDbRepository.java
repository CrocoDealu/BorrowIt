package org.example.borrowit.repository;

import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.Rental;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RentalDbRepository implements RentalRepository {
    private static final Logger logger = LogManager.getLogger(RentalDbRepository.class);
    private final SessionFactory sessionFactory;

    @Autowired
    public RentalDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Iterable<Rental> findRentalsByUserId(int userId) {
        logger.info("Finding rentals for user with id {}", userId);
        String hql = "FROM Rental r WHERE r.user.id = :userId";
        Iterable<Rental> rentals;

        try (Session session = getSession()) {
            rentals = session.createQuery(hql, Rental.class).setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding rentals by user: " + e.getMessage());
        }
        logger.info("Found rentals for user {}", userId);
        return rentals;
    }

    @Override
    public Iterable<Rental> findAll() {
        logger.info("Finding all rentals");
        String hql = "FROM Rental";
        Iterable<Rental> rentals;

        try (Session session = getSession()) {
            rentals = session.createQuery(hql, Rental.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding rentals: " + e.getMessage());
        }
        logger.info("Found all rentals");
        return rentals;
    }

    @Override
    public Optional<Rental> findById(Integer integer) {
        logger.info("Finding rental by id {}", integer);
        try (Session session = getSession()) {
           Rental rental = session.get(Rental.class, integer);
           logger.info("Found rental {}", rental);
           return Optional.of(rental);
        } catch (NoResultException e) {
            logger.warn("No rental found with id {}", integer);
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding rental by id: " + e.getMessage());
        }
    }

    @Override
    public Rental save(Rental entity) {
        logger.info("Saving rental {}", entity);
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error saving rental: " + e.getMessage());
        }
        logger.info("Saved rental {}", entity);
        return entity;
    }

    @Override
    public Optional<Rental> deleteById(Integer integer) {
        logger.info("Deleting rental {}", integer);
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            Rental rental = session.get(Rental.class, integer);
            if (rental != null) {
                session.delete(rental);
                transaction.commit();
                logger.info("Deleted rental {}", rental);
                return Optional.of(rental);
            } else {
                logger.warn("No rental found with id {}", integer);
                return Optional.empty();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting rental by id: " + integer + " " + e.getMessage());
        }
    }

    @Override
    public Rental update(Rental entity) {
        logger.info("Updating rental {}", entity);
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating rental: " + e.getMessage());
        }
        logger.info("Updated rental {}", entity);
        return entity;
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }
}
