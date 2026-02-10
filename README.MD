# 🔄 SkillSwap: Knowledge Exchange Platform

SkillSwap is a RESTful backend service built with Spring Boot that allows students to trade skills using a virtual
credit system. Instead of paying with money, users trade "Skill Credits" earned by teaching others.

## 🚀 Key Features

* **Transactional Credit System:** Securely transfers credits between users using `@Transactional` to ensure data
  integrity.
* **Skill Discovery:** API endpoints to browse available skills with provider details.
* **Automated Documentation:** Fully interactive API playground powered by Swagger UI.
* **Testing Suite:** Robust integration tests using MockMvc to verify business logic and credit boundaries.

## 🛠 Tech Stack

* **Java 21**
* **Spring Boot 3.4**
* **Spring Data JPA** (Hibernate)
* **H2 Database** (In-memory for development)
* **Swagger/OpenAPI** (SpringDoc)
* **Lombok** & **Java Records**

## 📖 API Documentation

Once the application is running, you can access the interactive Swagger UI at:
`http://localhost:8080/swagger-ui.html`

### Primary Endpoints:

| Method | Endpoint           | Description                                           |
|:-------|:-------------------|:------------------------------------------------------|
| `GET`  | `/api/skills`      | List all available skills (provider passwords hidden) |
| `POST` | `/api/skills/swap` | Exchange credits for a skill performance              |

## 🚦 Getting Started

### Prerequisites

* JDK 21 or higher
* Maven 3.x