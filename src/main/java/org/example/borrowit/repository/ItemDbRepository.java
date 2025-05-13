package org.example.borrowit.repository;

import org.example.borrowit.domain.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class ItemDbRepository implements ItemRepository {
    private final SessionFactory sessionFactory;

    public ItemDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<Item> findAll() {
        String hql = "from Item";
        Iterable<Item> items;
        try (Session session = getSession()) {
            items = session.createQuery(hql, Item.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            items = null;
        }
        return items;
    }

    @Override
    public Optional<Item> findById(Integer integer) {
        try (Session session = getSession()) {
            Item item = session.find(Item.class, integer);
            return Optional.ofNullable(item);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Item save(Item entity) {
        return null;
    }

    @Override
    public Optional<Item> deleteById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Item update(Item entity) {
        return null;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }
}
