package com.example.nutrition_info_service.repository;

import com.example.nutrition_info_service.model.entity.FoodItem;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    boolean existsByNameIgnoreCase(String name);
}
