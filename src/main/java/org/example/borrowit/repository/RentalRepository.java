package org.example.borrowit.repository;

import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.Rental;

public interface RentalRepository extends IRepository<Integer, Rental> {
    /**
     * Retrieve all rentals associated with a specific user ID.
     * @param userId the ID of the user whose rentals are to be retrieved.
     * @return an iterable collection of rentals associated with the specified user ID.
     */
    Iterable<Rental> findRentalsByUserId(int userId);

}
