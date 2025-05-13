package org.example.borrowit.service;

import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.Review;
import org.example.borrowit.domain.User;
import org.example.borrowit.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return StreamSupport.stream(reviewRepository.findAll().spliterator(), false).toList();
    }

    public List<Review> getReviewsByItemId(Item item) {
        return StreamSupport.stream(reviewRepository.findByItem(item.getId()).spliterator(), false).toList();
    }

    public List<Review> getReviewsByUserId(User user) {
        return StreamSupport.stream(reviewRepository.findByUser(user.getId()).spliterator(), false).toList();
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }
}
