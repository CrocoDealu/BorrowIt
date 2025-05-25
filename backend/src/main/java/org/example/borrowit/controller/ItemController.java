package org.example.borrowit.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.borrowit.domain.Item;
import org.example.borrowit.domain.User;
import org.example.borrowit.dto.ItemWithImagesDto;
import org.example.borrowit.service.ItemService;
import org.example.borrowit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ItemWithImagesDto>> getAllItems() {
        List<Item> items = itemService.getAllItems();

        List<ItemWithImagesDto> itemsWithImages = items.stream()
                .map(item -> {
                    List<byte[]> imagesAsBinary = item.getImages().stream()
                            .map(imagePath -> {
                                try {
                                    Path filePath = Paths.get("images/", imagePath);
                                    return Files.readAllBytes(filePath);
                                } catch (IOException e) {
                                    System.err.println("Failed to read image: " + imagePath);
                                    e.printStackTrace();
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .toList();

                    return new ItemWithImagesDto(item, imagesAsBinary);
                })
                .toList();
        return ResponseEntity.ok(itemsWithImages);
    }

    @GetMapping("/id")
    public ResponseEntity<Item> getItemById(@RequestParam Integer id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/token")
    public ResponseEntity<List<Item>> getItemsByUserId(@RequestParam String authToken) {
        if (userService.getUserByToken(authToken) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.getUserByToken(authToken);
        List<Item> items = itemService.getAllItemsForUser(user.getId());
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(items);
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Item> addItem(
            @RequestPart("item") Item item,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestHeader("Authorization") String token
    ) {
        User currentUser = userService.getUserByToken(token);
        item.setOwner(currentUser);

        List<String> images = new ArrayList<>();
        System.out.println("Request body with user: " + item);
        if (files != null && !files.isEmpty()) {
            images = saveImages(files, images);
        }
        item.setImages(images);
        Item createdItem = itemService.addItem(item);

        return ResponseEntity.status(201).body(createdItem);
    }

    private static List<String> saveImages(List<MultipartFile> files, List<String> images) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String uploadDir = "images/";
            Path filePath = Paths.get(uploadDir, fileName);

            try {
                if (!Files.exists(filePath)) {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    System.out.println("File already exists: " + filePath.toString());
                }

                images.add(fileName);
            } catch (IOException e) {
                System.err.println("Failed to save file: " + fileName);
                e.printStackTrace();
            }
        }
        return images;
    }

    @DeleteMapping("/id")
    public ResponseEntity<Item> deleteItemById(@RequestParam Integer id) {
        Optional<Item> deletedItem = itemService.deleteItem(id);
        return deletedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
