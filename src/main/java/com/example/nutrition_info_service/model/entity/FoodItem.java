package com.example.nutrition_info_service.model.entity;

import com.example.nutrition_info_service.model.enums.FoodCategory;
import com.example.nutrition_info_service.model.enums.ServingUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private double calories;
    @Column(nullable = false)
    private double protein;
    @Column(nullable = false)
    private double carbs;
    @Column(nullable = false)
    private double fat;
    @Column(nullable = false, name = "serving_size")
    private double servingSize;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "serving_unit")
    private ServingUnit servingUnit;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category;
}
