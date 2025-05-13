package org.example.borrowit.repository;

import org.example.borrowit.domain.Item;

public interface InsuranceRepository {
    boolean isInsured(Item item);
}
