package org.example.borrowit.controller;

import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.Rental;
import org.example.borrowit.domain.User;
import org.example.borrowit.dto.RentalDatesDto;
import org.example.borrowit.dto.RentalDto;
import org.example.borrowit.service.ItemService;
import org.example.borrowit.service.RentalService;
import org.example.borrowit.service.UserService;
import org.example.borrowit.utils.RentalStatus;
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

    @GetMapping("/user")
    public ResponseEntity<List<RentalDto>> getRentalsForUser(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Rental> rentals = rentalService.getRentalsByUserId(user.getId());
        List<RentalDto> rentalDtos = rentals.stream().map(RentalDto::new).toList();
        return ResponseEntity.ok(rentalDtos);
    }

    @GetMapping("/id")
    public ResponseEntity<RentalDto> getRentalsId(Integer rentalId) {
        Optional<Rental> rental = rentalService.getRentalById(rentalId);
        return rental.map(r -> ResponseEntity.ok(new RentalDto(r))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<List<RentalDatesDto>> getRentalsForItem(@PathVariable("id") Integer itemId) {
        Optional<Item> item = itemService.getItemById(itemId);
        if (item.isEmpty()) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        List<Rental> rentals = rentalService.getRentalsByItemId(itemId);
        List<RentalDatesDto> rentalDtos = rentals.stream().map(RentalDatesDto::new).toList();
        return ResponseEntity.ok(rentalDtos);
    }

    @PutMapping("/mark-as-returned/{rentalId}")
    public ResponseEntity<?> markAsMarkReturned(@PathVariable("rentalId") Integer id, @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Rental> rentalOpt = rentalService.getRentalById(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Rental rental = rentalOpt.get();
        if (!rental.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to mark this rental as returned");
        }
        rental.setStatus(RentalStatus.MARKED_AS_RETURNED);
        Rental updatedRental = rentalService.updateRental(rental);
        return ResponseEntity.ok(new RentalDto(updatedRental));
    }

    @PutMapping("returned/{rentalId}")
    public ResponseEntity<?> markAsReturned(@PathVariable("rentalId") Integer id, @RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Rental> rentalOpt = rentalService.getRentalById(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Rental rental = rentalOpt.get();
        if (!rental.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to mark this rental as returned");
        }
        rental.setStatus(RentalStatus.RETURNED);
        Rental updatedRental = rentalService.updateRental(rental);
        return ResponseEntity.ok(new RentalDto(updatedRental));
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
