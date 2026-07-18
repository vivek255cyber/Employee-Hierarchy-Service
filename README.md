# Employee-Hierarchy-Service

# Employee Hierarchy Management API

## Overview

Employee Hierarchy Management API is a Spring Boot REST application that demonstrates JWT-based authentication and role-based authorization for an employee reporting hierarchy.

The application allows:

- Employees to view only their own profile.
- Managers to view their own profile along with all employees reporting under their hierarchy.

The project is built as an interview assignment to showcase Spring Boot, Spring Security, JWT authentication, JPA, H2 Database, and hierarchical data retrieval.

---

## Technology Stack

- Java 21
- Spring Boot 3.3.4
- Spring Security
- Spring Data JPA
- JWT (JJWT 0.12.6)
- H2 Database
- Maven
- Lombok

---

## Features

- JWT Authentication
- Stateless Security
- Employee Profile Retrieval
- Manager Hierarchy Retrieval
- Recursive Employee Hierarchy
- H2 In-Memory Database
- REST APIs
- Spring Security Integration

---

## Project Structure

```
src
├── controller
├── dto
├── entity
├── repository
├── security
├── service
└── EmployeeHierarchyApplication.java
```

---

## Authentication

The application authenticates users using a JWT token.

The JWT contains the following claims:

```json
{
  "userId": 1,
  "username": "manager1",
  "loginId": "alice.manager",
  "workspaceId": "MANAGER"
}
```

The `workspaceId` claim determines the type of user.

- MANAGER
- EMPLOYEE

---

## Business Rules

### Employee

- Can access only their own profile.
- Cannot access details of other employees.

Response:

```json
{
  "userProfile": {
    ...
  },
  "message": "Employee details retrieved successfully"
}
```

---

### Manager

- Can access their own profile.
- Can access every employee in their reporting hierarchy.

Response:

```json
{
  "userProfile": {
    ...
  },
  "employees": [
    ...
  ],
  "message": "Manager details retrieved successfully"
}
```

---

# Running the Application

## Clone the repository

```bash
git clone <repository-url>
```

## Build the project

```bash
mvn clean install
```

## Run the application

```bash
mvn spring-boot:run
```

or run

```
EmployeeHierarchyApplication.java
```

from your IDE.

The application starts on

```
http://localhost:8080
```

---

# H2 Database

H2 Console

```
http://localhost:8080/h2-console
```

Database Configuration

| Property | Value |
|----------|-------|
| Driver Class | org.h2.Driver |
| JDBC URL | jdbc:h2:mem:testdb |
| Username | sa |
| Password | *(blank)* |

---

# API

## Get User Details

```
GET /api/user/getDetails
```

### Header

```
Authorization: Bearer <JWT_TOKEN>
```

Example

```bash
curl --location 'http://localhost:8080/api/user/getDetails' \
--header 'Authorization: Bearer <JWT_TOKEN>'
```

---

# Sample Responses

## Employee Response

```json
{
  "userProfile": {
    "userId": 3,
    "firstName": "Charlie",
    "lastName": "Employee",
    "loginId": "charlie.employee",
    "country": "USA",
    "parentId": 1,
    "manager": false
  },
  "message": "Employee details retrieved successfully"
}
```

---

## Manager Response

```json
{
  "userProfile": {
    "userId": 1,
    "firstName": "Alice",
    "lastName": "Manager",
    "loginId": "alice.manager",
    "country": "USA",
    "parentId": 1,
    "manager": true
  },
  "employees": [
    {
      "userId": 3,
      "firstName": "Charlie",
      "lastName": "Employee"
    },
    {
      "userId": 4,
      "firstName": "Diana",
      "lastName": "Employee"
    }
  ],
  "message": "Manager details retrieved successfully"
}
```

---

# Tested Scenarios

### Scenario 1 – Single Employee

```
Charlie
```

Expected:

- Employee can retrieve only their own profile.

---

### Scenario 2 – Manager with No Employees

```
Alice
```

Expected:

- Manager profile is returned.
- Employee list is empty.

---

### Scenario 3 – One Manager with Multiple Employees

```
Alice
├── Charlie
└── Diana
```

Expected:

- Manager receives own profile.
- Manager receives all direct employees.

---

### Scenario 4 – Multiple Managers

```
Alice
├── Charlie
└── Diana

Bob
└── Eve
```

Expected:

- Each manager can view only their own hierarchy.

---

### Scenario 5 – Multi-Level Hierarchy

```
CEO
└── Manager
     └── Team Lead
          ├── Employee 1
          └── Employee 2
```

Expected:

- Recursive hierarchy retrieval works correctly.

---

### Scenario 6 – Large Team

```
Manager
├── Employee 1
├── Employee 2
├── Employee 3
├── Employee 4
├── Employee 5
└── Employee 6
```

Expected:

- All employees are returned.

---

### Scenario 7 – Deep Hierarchy

```
CEO
└── Manager
     └── Lead
          └── Senior
               └── Junior
```

Expected:

- Complete recursive hierarchy is returned.

---

### Scenario 8 – Employee Reassignment

Employee moved from one manager to another.

Expected:

- Updated reporting hierarchy is reflected.

---

### Scenario 9 – Invalid Parent Reference

Employee references a non-existent manager.

Expected:

- Application handles invalid hierarchy gracefully.

---

# Security

- JWT Authentication
- Stateless Session Management
- Authorization Header Validation
- Token Validation
- Spring Security Filter Chain
- Role determination using the `workspaceId` claim in the JWT

---

# Assumptions

- Every request contains a valid JWT.
- `workspaceId` identifies whether the authenticated user is a **MANAGER** or **EMPLOYEE**.
- Employee hierarchy is maintained using the `PARENT_ID` field in the `USER_PROFILES` table.
- Managers can access only employees belonging to their reporting hierarchy.
- Employees can access only their own profile.

---


---

## Author

Employee Hierarchy Management API

Spring Boot Interview Assignment
