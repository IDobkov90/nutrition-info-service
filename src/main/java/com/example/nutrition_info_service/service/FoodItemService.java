package com.example.nutrition_info_service.service;

import com.example.nutrition_info_service.mapper.FoodItemMapper;
import com.example.nutrition_info_service.model.dto.FoodItemDTO;
import com.example.nutrition_info_service.model.entity.FoodItem;
import com.example.nutrition_info_service.repository.FoodItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodItemService {

    private static final String FOOD_ITEM_NOT_FOUND_MESSAGE = "Food item not found with id: ";

    private final FoodItemRepository foodItemRepository;
    private final FoodItemMapper foodItemMapper;

    @Transactional(readOnly = true)
    public long count() {
        return foodItemRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<FoodItemDTO> getAllFoodItems(Pageable pageable) {
        Page<FoodItem> foodItemPage = foodItemRepository.findAll(pageable);
        return foodItemPage.map(foodItemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(foodItemMapper::toDto)
                .toList();
    }

    @Transactional
    public FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO) {
        if (foodItemRepository.existsByNameIgnoreCase(foodItemDTO.getName())) {
            throw new IllegalArgumentException("A food item with this name already exists");
        }
        validateNutritionalValues(foodItemDTO);
        foodItemDTO.setId(null);
        FoodItem foodItem = foodItemMapper.toEntity(foodItemDTO);
        foodItem = foodItemRepository.save(foodItem);
        return foodItemMapper.toDto(foodItem);
    }

    @Transactional
    public FoodItemDTO updateFoodItem(FoodItemDTO foodItemDTO) {
        if (foodItemDTO.getId() == null) {
            throw new IllegalArgumentException("ID cannot be null for update operation");
        }

        FoodItem existingItem = foodItemRepository.findById(foodItemDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(FOOD_ITEM_NOT_FOUND_MESSAGE + foodItemDTO.getId()));

        validateNutritionalValues(foodItemDTO);

        foodItemMapper.updateEntityFromDto(foodItemDTO, existingItem);

        FoodItem savedItem = foodItemRepository.save(existingItem);

        return foodItemMapper.toDto(savedItem);
    }

    private void validateNutritionalValues(FoodItemDTO dto) {
        double totalGrams = dto.getProtein() + dto.getCarbs() + dto.getFat();
        if ("GRAM".equalsIgnoreCase(dto.getServingUnit()) && totalGrams > dto.getServingSize()) {
            throw new IllegalArgumentException("Total macronutrients cannot exceed serving size");
        }
        double calculatedCalories = (dto.getProtein() * 4) + (dto.getCarbs() * 4) + (dto.getFat() * 9);
        double allowedDeviation = 10;
        if (Math.abs(calculatedCalories - dto.getCalories()) > allowedDeviation) {
            throw new IllegalArgumentException("Calories don't match the macronutrient values");
        }
    }
}
