package org.example.borrowit.repository;

import org.example.borrowit.domain.Review;

public interface ReviewRepository extends IRepository<Integer, Review> {
    Iterable<Review> findByUser(int userId);
    Iterable<Review> findByItem(int itemId);
}
