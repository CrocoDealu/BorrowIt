package org.example.borrowit.repository;

import org.example.borrowit.domain.Transaction;

public interface TransactionRepository extends IRepository<Integer, Transaction> {
    Iterable<Transaction> findTransactionsByUserId(int userId);
}
