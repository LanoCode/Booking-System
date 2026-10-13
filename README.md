# 🎯 Booking System Backend

A robust, production-ready booking system backend built with **Java 17** and **Spring Boot 3**.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![License](https://img.shields.io/badge/License-MIT-blue)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Testing](#-testing)
- [Author](#-author)

## ✨ Features

- 🔐 JWT Authentication & Authorization
- 📅 Booking Management with Conflict Detection
- 🔍 Advanced Filtering & Pagination
- 📊 Swagger/OpenAPI Documentation
- ✅ Input Validation with Jakarta Validation
- 🧪 Comprehensive Test Coverage
- 🗄️ PostgreSQL Database with JPA/Hibernate

## 🛠 Tech Stack

| Technology | Version |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.x |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| PostgreSQL | 14+ |
| Gradle | 8.x |
| JWT (jjwt) | 0.11.x |

## 🚀 Getting Started

### Prerequisites

- JDK 17+
- PostgreSQL 14+
- Gradle 8+ (or use included Gradle wrapper)

### Installation

```bash
# Clone the repository
git clone https://github.com/LanoCode/Booking-System.git
cd Booking-System

# Configure database in src/main/resources/application.yml

# Build the project
./gradlew build          # Linux/Mac
gradlew.bat build        # Windows

# Run the application
./gradlew bootRun        # Linux/Mac
gradlew.bat bootRun      # Windows
