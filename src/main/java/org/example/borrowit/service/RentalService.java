package org.example.borrowit.service;

import org.example.borrowit.domain.Rental;
import org.example.borrowit.repository.RentalRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class RentalService {
    private final RentalRepository rentalRepository;


    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> getAllRentals() {
        return StreamSupport.stream(rentalRepository.findAll().spliterator(), false).toList();
    }

    public List<Rental> getRentalsByUserId(int userId) {
        return StreamSupport.stream(rentalRepository.findRentalsByUserId(userId).spliterator(), false).toList();
    }

    public Rental addRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public Rental updateRental(Rental rental) {
        return rentalRepository.update(rental);
    }

    public Optional<Rental> deleteRental(int rentalId) {
        return rentalRepository.deleteById(rentalId);
    }

    public Optional<Rental> getRentalById(int rentalId) {
        return rentalRepository.findById(rentalId);
    }
}
