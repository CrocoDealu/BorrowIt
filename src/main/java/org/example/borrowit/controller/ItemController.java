package org.example.borrowit.controller;

import org.example.borrowit.domain.Item;
import org.example.borrowit.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/id")
    public ResponseEntity<Item> getItemById(@RequestParam Integer id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/id")
    public ResponseEntity<List<Item>> getItemsByUserId(@RequestParam Integer userId) {
        List<Item> items = itemService.getAllItemsForUser(userId);
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(items);
        }
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item createdItem = itemService.addItem(item);
        return ResponseEntity.status(201).body(createdItem);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Item> deleteItemById(@RequestParam Integer id) {
        Optional<Item> deletedItem = itemService.deleteItem(id);
        return deletedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
