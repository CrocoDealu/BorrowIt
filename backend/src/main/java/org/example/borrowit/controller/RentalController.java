package org.example.borrowit.controller;

import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.Rental;
import org.example.borrowit.domain.User;
import org.example.borrowit.dto.RentalDto;
import org.example.borrowit.service.ItemService;
import org.example.borrowit.service.RentalService;
import org.example.borrowit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private final ItemService itemService;

    public RentalController(RentalService rentalService, UserService userService, ItemService itemService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<RentalDto>> getRentals() {
        return ResponseEntity.ok().body(rentalService.getAllRentals().stream().map(RentalDto::new).toList());
    }

    @GetMapping("/user/token")
    public ResponseEntity<List<RentalDto>> getRentalsForUser(@RequestHeader("Authorization") String token) {
        int userId = 0;
        List<Rental> rentals = rentalService.getRentalsByUserId(userId);
        List<RentalDto> rentalDtos = rentals.stream().map(RentalDto::new).toList();
        if (rentalDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(rentalDtos);
        }
    }

    @GetMapping("/item/id")
    public ResponseEntity<RentalDto> getRentalsId(Integer rentalId) {
        Optional<Rental> rental = rentalService.getRentalById(rentalId);
        return rental.map(r -> ResponseEntity.ok(new RentalDto(r))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/rent/{itemId}")
    public ResponseEntity<?> createRental(@RequestBody Rental rental, @RequestHeader("Authorization") String token, @PathVariable Integer itemId) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByToken(token);
        rental.setUser(user);
        Optional<Item> optItem = itemService.getItemById(itemId);
        if (optItem.isEmpty()) {
            return ResponseEntity.badRequest().body("Item not found");
        }
        Item item = optItem.get();
        rental.setItem(item);
        System.out.println(rental);
        Rental createdRental = rentalService.addRental(rental);
        if (createdRental != null) {
            return ResponseEntity.ok(new RentalDto(createdRental));
        } else {
            return ResponseEntity.badRequest().body("Failed to create rental");
        }
    }
}
