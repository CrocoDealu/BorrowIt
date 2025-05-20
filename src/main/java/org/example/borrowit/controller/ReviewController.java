package org.example.borrowit.controller;

import org.example.borrowit.domain.Review;
import org.example.borrowit.service.ItemService;
import org.example.borrowit.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Review> getReviewById(@RequestParam("id") int id) {
        return reviewService.getReviewById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review createdReview = reviewService.addReview(review);
        return ResponseEntity.status(201).body(createdReview);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Review> deleteReviewById(@RequestParam("id") int id) {
        Optional<Review> deletedReview = reviewService.getReviewById(id);
        return deletedReview.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/item/id")
    public ResponseEntity<List<Review>> getReviewsByItemId(@RequestParam("itemId") int itemId) {
        List<Review> reviews = reviewService.getReviewsByItemId(itemId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/id")
    public ResponseEntity<List<Review>> getReviewsByUserId(@RequestParam("userId") int userId) {
        List<Review> userReviews = reviewService.getReviewsByUserId(userId);
        if (userReviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userReviews);
    }
}
