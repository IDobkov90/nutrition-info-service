package com.example.nutrition_info_service.init;

import com.example.nutrition_info_service.model.dto.FoodItemDTO;
import com.example.nutrition_info_service.service.FoodItemService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private static final String FOOD_ITEMS_JSON_PATH = "src/main/resources/json/food-items-seed.json";
    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final FoodItemService foodItemService;
    private final Gson gson;

    @Override
    public void run(String... args) throws Exception {
        if (isDatabaseEmpty()) {
            log.info("Seeding database...");
            List<FoodItemDTO> foodItems = loadFoodItemsFromJson();
            for (FoodItemDTO foodItem : foodItems) {
                try {
                    foodItemService.createFoodItem(foodItem);
                } catch (Exception e) {
                    log.error("Error seeding food item: {}. {}", foodItem.getName(), e.getMessage());
                }
            }
            log.info("Database seeding completed.");
        } else {
            log.info("Database is not empty. Skipping seeding.");
        }
    }

    private boolean isDatabaseEmpty() {
        return foodItemService.count() == 0;
    }

    private List<FoodItemDTO> loadFoodItemsFromJson() throws FoodItemLoadException {
        try {
            String jsonContent = new String(Files.readAllBytes(Path.of(FOOD_ITEMS_JSON_PATH)));
            Type foodItemListType = new TypeToken<List<FoodItemDTO>>() {
            }.getType();
            return gson.fromJson(jsonContent, foodItemListType);
        } catch (IOException e) {
            throw new FoodItemLoadException("Error loading food items from JSON file", e);
        }
    }

    private static class FoodItemLoadException extends Exception {
        public FoodItemLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
