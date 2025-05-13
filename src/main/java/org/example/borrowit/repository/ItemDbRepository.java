package org.example.borrowit.repository;

import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class ItemDbRepository implements ItemRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(ItemDbRepository.class);

    public ItemDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<Item> findAll() {
        logger.info("Finding all items...");
        String hql = "from Item";
        Iterable<Item> items;
        try (Session session = getSession()) {
            items = session.createQuery(hql, Item.class).getResultList();
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException("Error finding items..." + e.getMessage());
        }
        logger.info("Found all items");
        return items;
    }

    @Override
    public Optional<Item> findById(Integer integer) {
        logger.info("Finding item by id: {}", integer);
        try (Session session = getSession()) {
            Item item = session.find(Item.class, integer);
            logger.info("Found the item by id: {}", integer);
            return Optional.ofNullable(item);
        } catch (NoResultException e) {
            logger.warn("No item found with id: {}", integer);
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding item by id: " + integer);
        }
    }

    @Override
    public Item save(Item entity) {
        logger.info("Saving item: {}", entity);
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving item: {}", entity, e);
        }
        logger.info("Saved item: {}", entity);
        return entity;
    }

    @Override
    public Optional<Item> deleteById(Integer integer) {
        logger.info("Deleting item by id: {}", integer);
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            Item item = session.find(Item.class, integer);
            if (item != null) {
                session.delete(item);
                transaction.commit();
                logger.info("Deleted item by id: {}", integer);
                return Optional.of(item);
            } else {
                logger.warn("No item with id: {} found", integer);
                return Optional.empty();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting item by id: " + integer);
        }
    }

    @Override
    public Item update(Item entity) {
        logger.info("Updating item: {}", entity);
        Transaction transaction = null;
        Item updatedItem = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            updatedItem = session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating item: " + entity, e);
        }
        logger.info("Updated item: {}", entity);
        return updatedItem;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
