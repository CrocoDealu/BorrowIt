package org.example.borrowit.controller;

import org.example.borrowit.domain.Rental;
import org.example.borrowit.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<Rental>> getRentals() {
        return ResponseEntity.ok().body(rentalService.getAllRentals());
    }

    @GetMapping("/user/token")
    public ResponseEntity<List<Rental>> getRentalsForUser(String token) {
        int userId = 0;
        List<Rental> rentals = rentalService.getRentalsByUserId(userId);
        if (rentals.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(rentals);
        }
    }

    @GetMapping("/item/id")
    public ResponseEntity<Rental> getRentalsId(Integer rentalId) {
        Optional<Rental> rental = rentalService.getRentalById(rentalId);
        return rental.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
