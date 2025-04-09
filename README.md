# Nutrition Info Service

A Spring Boot microservice that provides nutritional information for various food items. This service is designed to be consumed by the Calorator project.

## Features

- CRUD operations for food items
- Pagination and sorting support
- Input validation
- Database persistence with MySQL
- JSON-based data seeding

## Tech Stack

- Java 23
- Spring Boot 3.4.3
- Spring Data JPA
- MySQL
- Maven
- Lombok
- MapStruct
- HATEOAS

## Prerequisites

- JDK 23
- MySQL 8.0+
- Maven 3.6+

### Database Setup

The service requires a MySQL database. Configure your database connection in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nutrition_info_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Set the following environment variables:
- `DB_USERNAME`: Your MySQL username
- `DB_PASSWORD`: Your MySQL password

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
./mvnw spring-boot:run
```

The service will start on port 8081 by default.

## API Endpoints

### Get Food Items
```http
GET /api/food-items?page=0&size=10&sortBy=name&direction=asc
```

### Create Food Item
```http
POST /api/food-items
Content-Type: application/json

{
    "name": "String",
    "calories": "Double",
    "protein": "Double",
    "carbs": "Double",
    "fat": "Double",
    "servingSize": "Double",
    "servingUnit": "String (GRAM|MILLILITER|OUNCE|CUP|TABLESPOON|TEASPOON|PIECE)",
    "category": "String (FRUITS|VEGETABLES|GRAINS|PROTEIN|DAIRY|FATS|SWEETS|BEVERAGES|OTHER)"
}
```

### Update Food Item
```http
PUT /api/food-items/{id}
Content-Type: application/json

{
    // Same structure as POST
}
```

## Data Validation

The service performs the following validations:
- Unique food item names (case-insensitive)
- Nutritional values consistency check
- Caloric value validation against macronutrients
- Required fields validation

## Initial Data

The service comes with a pre-configured set of food items in `food-items-seed.json` that will be loaded on first startup when the database is empty.
