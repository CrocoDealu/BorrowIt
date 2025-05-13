package org.example.borrowit.repository;

import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class ReviewDbRepository implements ReviewRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(ReviewDbRepository.class);

    @Autowired
    public ReviewDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<Review> findByUser(int userId) {
        logger.info("Finding reviews by user {}", userId);
        String hql = "FROM Review r WHERE r.user.id = :userId";
        Iterable<Review> reviews;

        try (Session session = getSession()) {
            reviews = session.createQuery(hql, Review.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding reviews by user: " + e.getMessage());
        }
        logger.info("Found reviews for user {}: {}", userId, reviews);
        return reviews;
    }

    @Override
    public Iterable<Review> findByItem(int itemId) {
        logger.info("Finding reviews by item {}", itemId);
        String hql = "FROM Review r WHERE r.item.id = :itemId";
        Iterable<Review> reviews;

        try (Session session = getSession()) {
            reviews = session.createQuery(hql, Review.class)
                    .setParameter("itemId", itemId)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding reviews by item: " + e.getMessage());
        }
        logger.info("Found reviews for item {}: {}", itemId, reviews);
        return reviews;
    }

    @Override
    public Iterable<Review> findAll() {
        logger.info("Finding all reviews");
        String hql = "FROM Review";
        Iterable<Review> reviews;

        try (Session session = getSession()) {
            reviews = session.createQuery(hql, Review.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all reviews: " + e.getMessage());
        }
        logger.info("Found reviews: {}", reviews);
        return reviews;
    }

    @Override
    public Optional<Review> findById(Integer integer) {
        logger.info("Finding review by id {}", integer);
        try (Session session = getSession()) {
            Review review = session.get(Review.class, integer);
            logger.info("Found review by id {}: {}", integer, review);
            return Optional.ofNullable(review);
        } catch (NoResultException e) {
            logger.warn("No review found with id {}", integer);
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding review by id: " + integer);
        }
    }

    @Override
    public Review save(Review entity) {
        logger.info("Saving review {}", entity);
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving review: " + e.getMessage());
        }
        logger.info("Saved review {}", entity);
        return entity;
    }

    @Override
    public Optional<Review> deleteById(Integer integer) {
        logger.info("Deleting review by id {}", integer);
        try (Session session = getSession()) {
            session.beginTransaction();
            Review review = session.get(Review.class, integer);
            if (review != null) {
                session.delete(review);
                session.getTransaction().commit();
                logger.info("Deleted review by id {}", integer);
                return Optional.of(review);
            } else {
                logger.warn("No review found with id {} to delete", integer);
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting review by id: " + integer + " " + e.getMessage());
        }
    }

    @Override
    public Review update(Review entity) {
        logger.info("Updating review {}", entity);
        try (Session session = getSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error updating review: " + e.getMessage());
        }
        logger.info("Updated review {}", entity);
        return entity;
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }
}
