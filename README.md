# Rewards API (Spring Boot + H2)

## Overview
This project calculates reward points for customers based on transactions.

### Rules
- 2 points for every $1 spent over $100
- 1 point for every $1 spent between $50 and $100

---

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- H2 Database

---

## How to Run
1. Open project in IntelliJ/Eclipse
2. Run `DemoApplication.java`
3. Access API:

GET http://localhost:8080/api-v2/rewards/{customerId}

Example:
GET http://localhost:8080/api-v2/rewards/C001

---

## H2 Console
http://localhost:8080/h2-console

JDBC URL:
jdbc:h2:mem:testdb

---

## Sample Output
```
{
  "customerId": "C001",
  "firstMonthPoints": 90,
  "secondMonthPoints": 250,
  "thirdMonthPoints": 60,
  "total": 400
}
```

---

## Notes
- Data is auto-loaded using data.sql
- In-memory DB resets on restart
