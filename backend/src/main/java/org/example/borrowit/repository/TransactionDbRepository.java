package org.example.borrowit.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TransactionDbRepository implements TransactionRepository {
    private static final Logger logger = LogManager.getLogger(TransactionDbRepository.class);
    private final SessionFactory sessionFactory;

    @Autowired
    public TransactionDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<Transaction> findTransactionsByUserId(int userId) {
        logger.info("Finding transactions by user id: " + userId);
        String hql = "FROM Transaction t WHERE t.rental.user.id = :userId";
        Iterable<Transaction> transactions;

        try (Session session = getSession()) {
            transactions = session.createQuery(hql).setParameter("userId", userId).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding transactions by user id: " + e.getMessage());
        }
        logger.info("Found transactions for user id {}", userId);
        return transactions;
    }

    @Override
    public Iterable<Transaction> findAll() {
        logger.info("Finding all transactions");
        String hql = "FROM Transaction";
        Iterable<Transaction> transactions;

        try (Session session = getSession()) {
            transactions = session.createQuery(hql).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all transactions: " + e.getMessage());
        }
        logger.info("Found all transactions");
        return transactions;
    }

    @Override
    public Optional<Transaction> findById(Integer integer) {
        logger.info("Finding transaction by id: " + integer);
        try (Session session = getSession()) {
            Transaction transaction = session.get(Transaction.class, integer);
            if (transaction != null) {
                logger.info("Found transaction with id: {}", integer);
                return Optional.of(transaction);
            } else {
                logger.warn("Transaction with id {} not found", integer);
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding transaction by id: " + e.getMessage());
        }
    }

    @Override
    public Transaction save(Transaction entity) {
        logger.info("Saving transaction: {}", entity);
        org.hibernate.Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            logger.info("Transaction saved successfully");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving transaction: " + e.getMessage());
        }
        logger.info("Transaction saved successfully: {}", entity);
        return entity;
    }

    @Override
    public Optional<Transaction> deleteById(Integer integer) {
        logger.info("Deleting transaction by id: {}", integer);
        try (Session session = getSession()) {
            session.beginTransaction();
            Transaction transaction = session.get(Transaction.class, integer);
            if (transaction != null) {
                session.delete(transaction);
                session.getTransaction().commit();
                logger.info("Transaction with id {} deleted successfully", integer);
                return Optional.of(transaction);
            } else {
                logger.warn("Transaction with id {} not found", integer);
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting transaction by id: " + e.getMessage());
        }
    }

    @Override
    public Transaction update(Transaction entity) {
        logger.info("Updating transaction: {}", entity);
        try (Session session = getSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
            logger.info("Transaction updated successfully: {}", entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error updating transaction: " + e.getMessage());
        }
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }
}
