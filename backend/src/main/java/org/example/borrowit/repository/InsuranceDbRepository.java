package org.example.borrowit.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InsuranceDbRepository implements InsuranceRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(InsuranceDbRepository.class);

    @Autowired
    public InsuranceDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isInsured(Item item) {
        String hql = "SELECT COUNT(it) FROM Insurance i JOIN i.items it WHERE it.id = :itemId";
        Query<Long> query = sessionFactory.openSession().createQuery(hql, Long.class);
        query.setParameter("rentalId", item.getId());

        Long count = query.uniqueResult();
        return count != null && count > 0;
    }
}
