package org.example.borrowit.service;

import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.Review;
import org.example.borrowit.domain.User;
import org.example.borrowit.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<Review> getReviewsByItemId(int itemId) {
        return StreamSupport.stream(reviewRepository.findByItem(itemId).spliterator(), false).toList();
    }

    public List<Review> getReviewsByUserId(int userId) {
        return StreamSupport.stream(reviewRepository.findByUser(userId).spliterator(), false).toList();
    }

    public Optional<Review> getReviewById(int id) {
        return reviewRepository.findById(id);
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }
}
