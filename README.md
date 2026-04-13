# Rewards API (Spring Boot + H2)

## Overview
A RESTful API that calculates reward points for customers based on their transaction history. Given a record of every transaction during a three-month period, the system calculates the reward points earned for each customer per month and total.

## Business Requirements

### Reward Calculation Rules
A retailer offers a rewards program to its customers, awarding points based on each recorded purchase:

- **2 points** for every dollar spent over $100
- **1 point** for every dollar spent between $50 and $100  
- **0 points** for amounts $50 or below

### Calculation Examples
| Transaction Amount | Calculation | Points Earned |
|-------------------|-------------|---------------|
| $120 | (20 × 2) + (50 × 1) = 40 + 50 | **90 points** |
| $100 | (0 × 2) + (50 × 1) = 0 + 50 | **50 points** |
| $75 | (0 × 2) + (25 × 1) = 0 + 25 | **25 points** |
| $50 | 0 | **0 points** |

---

## Tech Stack
- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: For data persistence
- **H2 Database**: In-memory database for development
- **Maven**: Build tool
- **BigDecimal**: For precise monetary calculations

---

## Project Structure
```
Rewards-Api/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/
│   │   │   │   └── RewardController.java
│   │   │   ├── dto/
│   │   │   │   ├── CustomerReward.java
│   │   │   │   ├── MonthlyReward.java
│   │   │   │   └── RewardResponse.java
│   │   │   ├── entity/
│   │   │   │   └── TransactionEntity.java
│   │   │   ├── exception/
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── InvalidDateRangeException.java
│   │   │   ├── repository/
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── service/
│   │   │   │   └── RewardService.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── java/com/example/demo/
│           ├── controller/
│           │   └── RewardControllerIntegrationTest.java
│           └── service/
│               └── RewardServiceTest.java
├── pom.xml
├── .gitignore
└── README.md
```

---

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Steps
1. **Clone or extract the project**
   ```bash
   cd Rewards-Api
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Or run `DemoApplication.java` from your IDE (IntelliJ IDEA / Eclipse)

4. **Application will start on**
   ```
   http://localhost:8080
   ```

---

## API Documentation

### Endpoint
```
GET /api/v1/rewards/calculate
```

### Description
Calculates reward points for **all customers** who have transactions within the specified date range. Returns monthly breakdown and total points for each customer.

### Request Parameters
| Parameter | Type | Required | Format | Description |
|-----------|------|----------|--------|-------------|
| `startDate` | String | Yes | YYYY-MM-DD | Start date of the period (ISO 8601) |
| `endDate` | String | Yes | YYYY-MM-DD | End date of the period (ISO 8601) |

### Validations
- Both `startDate` and `endDate` must be provided
- `startDate` must be before `endDate`
- Date range must not exceed 3 months
- Dates must be in ISO format (YYYY-MM-DD)

### Example Request
```bash
GET http://localhost:8080/api/v1/rewards/calculate?startDate=2026-01-01&endDate=2026-03-31
```

### Success Response (200 OK)
```json
[
  {
    "customerId": "C001",
    "monthlyRewards": [
      {"year": 2026, "month": 1, "points": 90},
      {"year": 2026, "month": 2, "points": 250},
      {"year": 2026, "month": 3, "points": 70}
    ],
    "totalPoints": 410
  },
  {
    "customerId": "C002",
    "monthlyRewards": [
      {"year": 2026, "month": 1, "points": 10},
      {"year": 2026, "month": 2, "points": 100},
      {"year": 2026, "month": 3, "points": 40}
    ],
    "totalPoints": 150
  },
  {
    "customerId": "C003",
    "monthlyRewards": [
      {"year": 2026, "month": 1, "points": 350},
      {"year": 2026, "month": 2, "points": 25},
      {"year": 2026, "month": 3, "points": 110}
    ],
    "totalPoints": 485
  }
]
```

### Response Codes
| Status Code | Description |
|-------------|-------------|
| `200 OK` | Rewards calculated successfully for one or more customers |
| `204 No Content` | No transactions found in the specified date range |
| `400 Bad Request` | Invalid request parameters (missing dates, invalid format, date range issues) |
| `500 Internal Server Error` | Unexpected server error |

### Error Response (400 Bad Request)
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "startDate must be before endDate"
}
```

### Error Response Examples
```json
// Missing required parameter
{
  "status": 400,
  "error": "Bad Request",
  "message": "Both startDate and endDate must be provided"
}

// Date range exceeds 3 months
{
  "status": 400,
  "error": "Bad Request",
  "message": "Date range must not exceed 3 months"
}

// Invalid date order
{
  "status": 400,
  "error": "Bad Request",
  "message": "startDate must be before endDate"
}
```

---

## Sample Data

The application comes pre-loaded with sample transaction data in `src/main/resources/data.sql`:

### Customer C001
- 2026-01-10: $120.00 → 90 points
- 2026-01-20: $80.00 → 30 points
- 2026-02-15: $200.00 → 250 points
- 2026-03-05: $45.00 → 0 points
- 2026-03-22: $110.00 → 70 points

### Customer C002
- 2026-01-08: $60.00 → 10 points
- 2026-02-12: $150.00 → 150 points
- 2026-02-25: $30.00 → 0 points
- 2026-03-18: $90.00 → 40 points

### Customer C003
- 2026-01-03: $250.00 → 350 points
- 2026-02-19: $75.00 → 25 points
- 2026-03-27: $130.00 → 110 points

---

## H2 Database Console

Access the in-memory H2 database console for debugging:

```
URL: http://localhost:8080/h2-console
```

**Connection Settings:**
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

### Useful SQL Queries
```sql
-- View all transactions
SELECT * FROM transactions;

-- View transactions for a specific customer
SELECT * FROM transactions WHERE customer_id = 'C001';

-- View transactions in a date range
SELECT * FROM transactions 
WHERE date BETWEEN '2026-01-01' AND '2026-03-31'
ORDER BY date;
```

---

## Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage
- **Unit Tests**: `RewardServiceTest.java`
  - Reward calculation logic for various amounts
  - Date validation scenarios
  - Edge cases (null, negative, boundary values)
  
- **Integration Tests**: `RewardControllerIntegrationTest.java`
  - Full HTTP request/response cycle
  - Valid date range scenarios
  - Error handling and validation

---

## Key Features

✅ **Multi-customer support** - Calculates rewards for all customers in the date range  
✅ **Dynamic date handling** - Works with any 3-month period, not hardcoded to specific months  
✅ **Year-aware** - Monthly breakdown includes year for cross-year date ranges  
✅ **Precise calculations** - Uses BigDecimal for accurate monetary computations  
✅ **Comprehensive validation** - Date range, format, and business rule validations  
✅ **RESTful design** - Follows REST principles with proper HTTP status codes  
✅ **Stream-based processing** - Modern Java 8+ functional programming style  
✅ **Fully tested** - Unit and integration tests included  

---

## Design Decisions

### Why BigDecimal?
Floating-point arithmetic (double/float) is imprecise for monetary calculations. BigDecimal ensures accurate financial computations.

### Why No Customer ID in Request?
Per requirements: "Given a record of every transaction during a three-month period, calculate the reward points earned for **each customer** per month and total." The API processes all customers within the date range.

### Why 3-Month Limit?
Business requirement specifies a three-month period. This prevents excessive data processing and aligns with typical quarterly reporting cycles.

### Why Constructor Injection?
Constructor injection (vs field injection) makes dependencies explicit, improves testability, and is the recommended Spring best practice.

---

## Notes

- Data is automatically loaded from `data.sql` on application startup
- H2 is an in-memory database - all data resets when the application restarts
- For production use, replace H2 with a persistent database (PostgreSQL, MySQL, etc.)
- All monetary amounts use BigDecimal to avoid floating-point precision issues
- The API returns rewards for **all customers** in the specified date range
- Monthly rewards are sorted chronologically (year, then month)

---

## Future Enhancements

- Add pagination for large result sets
- Support for filtering by specific customer ID (optional)
- Export rewards data to CSV/PDF
- Add caching for frequently requested date ranges
- Implement authentication and authorization
- Add database migration tool (Flyway/Liquibase)
- Containerize with Docker

---

## License

This project is for demonstration purposes.

---

## Contact

For questions or issues, please refer to the project documentation or create an issue in the repository.