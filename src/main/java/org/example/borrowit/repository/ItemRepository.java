package org.example.borrowit.repository;

import org.example.borrowit.domain.Item;
import org.example.borrowit.utils.ItemFilter;

import java.util.List;

public interface ItemRepository extends IRepository<Integer, Item> {
    public Iterable<Item> findByUserId(Integer userId);
}
