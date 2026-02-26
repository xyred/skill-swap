# 🔄 SkillSwap: Knowledge Exchange Platform

SkillSwap is a RESTful backend service built with **Spring Boot 4** that allows users to trade skills using a virtual
credit system. Instead of paying with money, users trade "Skill Credits" earned by teaching others.

## 🚀 Key Features

* **Transactional Credit System:** Securely transfers credits between users using `@Transactional` to ensure data
  integrity.
* **Skill Discovery:** API endpoints to browse available skills with provider details and title filtering.
* **Robust Error Handling:** Centralized global exception handling with professional SLF4J logging and detailed
  validation feedback.
* **Automated Documentation:** Fully interactive API playground powered by Swagger UI (OpenAPI 3).
* **Clean Architecture:** Strict separation of concerns using the Controller-Service-Repository pattern and DTOs.

## 🛠 Tech Stack

* **Java 21**
* **Spring Boot 4.0.2**
* **Spring Data JPA** (Hibernate)
* **H2 Database** (In-memory for development)
* **Swagger/OpenAPI** (SpringDoc)
* **Lombok** & **Java Records**

## ⚖️ Business Rules

To maintain the integrity of the exchange, the following rules are enforced at the service level:

1. **Credit Requirement:** A student must have at least **1 credit** to request a skill swap.
2. **Anti-Self-Swap:** Users cannot perform a swap on their own skills to prevent credit inflation.
3. **Atomic Transactions:** If the provider cannot be credited or the student cannot be debited, the entire operation
   rolls back.

## 📖 API Documentation

Once the application is running, you can access the interactive Swagger UI at:  
`http://localhost:8080/swagger-ui.html`

### Primary Endpoints:

| Method | Endpoint           | Description                                                |
|:-------|:-------------------|:-----------------------------------------------------------|
| `GET`  | `/api/skills`      | List all skills or filter by `search` parameter            |
| `GET`  | `/api/skills/{id}` | Get detailed information about a specific skill            |
| `POST` | `/api/skills/swap` | Exchange 1 credit for a skill (Requires studentId/skillId) |

## 🚦 Getting Started

### Prerequisites

* JDK 21 or higher
* Maven 3.x (or use the included Maven Wrapper)

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.