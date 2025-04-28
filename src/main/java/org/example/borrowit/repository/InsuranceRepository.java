package org.example.borrowit.repository;

import org.example.borrowit.domain.Rental;

public interface InsuranceRepository {
    boolean isInsured(Rental rental);
}
