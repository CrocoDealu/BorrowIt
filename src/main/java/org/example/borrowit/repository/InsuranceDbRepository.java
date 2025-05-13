package org.example.borrowit.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.borrowit.domain.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class InsuranceDbRepository implements InsuranceRepository {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(InsuranceDbRepository.class);

    public InsuranceDbRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isInsured(Rental rental) {
        String hql = "SELECT COUNT(r) FROM Insurance i JOIN i.rentals r WHERE r.id = :rentalId";
        Query<Long> query = sessionFactory.openSession().createQuery(hql, Long.class);
        query.setParameter("rentalId", rental.getId());

        Long count = query.uniqueResult();
        return count != null && count > 0;
    }
}
