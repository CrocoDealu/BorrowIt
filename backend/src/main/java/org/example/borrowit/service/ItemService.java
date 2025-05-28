package org.example.borrowit.service;

import org.example.borrowit.domain.Item;
import org.example.borrowit.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false).toList();
    }

    public List<Item> getAllItemsForUser(Integer userId) {
        return StreamSupport.stream(itemRepository.findByUserId(userId).spliterator(), false).toList();
    }

    public Optional<Item> getItemById(Integer itemId) {
        return itemRepository.findById(itemId);
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public Optional<Item> deleteItem(Integer itemId) {
        return itemRepository.deleteById(itemId);
    }

    public Item updateItem(Item item) {
        return itemRepository.update(item);
    }

    public List<Item> getItemsUserCanRent(int userid) {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false).filter(item -> item.getOwner().getId() != userid).toList();
    }
}
