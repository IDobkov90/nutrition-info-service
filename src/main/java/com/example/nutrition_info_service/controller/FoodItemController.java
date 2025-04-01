package com.example.nutrition_info_service.controller;

import com.example.nutrition_info_service.model.dto.FoodItemDTO;
import com.example.nutrition_info_service.service.FoodItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food-items")
@RequiredArgsConstructor
public class FoodItemController {

    private final FoodItemService foodItemService;

    @GetMapping
    public ResponseEntity<?> getAllFoodItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            PagedResourcesAssembler<FoodItemDTO> assembler) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<FoodItemDTO> foodItems = foodItemService.getAllFoodItems(pageable);
        return ResponseEntity.ok(assembler.toModel(foodItems));
    }

    @PostMapping
    public ResponseEntity<FoodItemDTO> createFoodItem(@Valid @RequestBody FoodItemDTO foodItemDTO) {
        FoodItemDTO createdFoodItem = foodItemService.createFoodItem(foodItemDTO);
        return new ResponseEntity<>(createdFoodItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItemDTO foodItemDTO) {
        foodItemDTO.setId(id);
        FoodItemDTO updatedFoodItem = foodItemService.updateFoodItem(foodItemDTO);
        return ResponseEntity.ok(updatedFoodItem);
    }
}
