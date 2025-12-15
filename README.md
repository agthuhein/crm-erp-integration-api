# CRM ↔ ERP Customer and Order Synchronization REST API

---

## 1. Introduction

This project implements a RESTful API to integrate **CRM** and **ERP** business systems.
The system focuses on synchronizing **customer and order data**, enabling reliable data exchange, process automation, and reporting across enterprise applications.

The solution is built using **Spring Boot**, **REST APIs**, and **MySQL**, following clean architecture and real-world enterprise integration best practices.

---

## 2. Objectives

- Design secure and scalable REST APIs
- Enable CRM-to-ERP order synchronization
- Prevent duplicate data processing
- Support reporting and system monitoring
- Demonstrate real-world enterprise integration patterns

---

## 3. Technologies Used

| Technology | Version |
|----------|---------|
| Java | **21 (LTS)** |
| Spring Boot | 3.x |
| Spring Data JPA (Hibernate) | 6.x |
| MySQL | 8.x |
| Maven | 3.x |
| RESTful APIs | HTTP / JSON |
| Postman | API Testing |
| CSV | Data export format |

Java 21 LTS is used to ensure long-term support, modern language features, and compatibility with Spring Boot 3.x.

---

## 4. System Overview

The system is divided into three logical components:

### CRM API
- Manages customers and order creation
- Acts as the source system for customer and order data

### ERP API
- Pulls new orders from CRM
- Acknowledges imported orders
- Updates order lifecycle status

### Integration API
- Bulk data import
- Webhook-based order status updates
- CSV export for reporting
- Health and metrics endpoints

---

## 5. Architecture Diagram

The system follows a layered, service-oriented architecture that simulates real-world integration between CRM and ERP systems using RESTful APIs.

### Logical Architecture Overview

```mathematica
┌────────────────────┐
│   Client / Postman │
└─────────┬──────────┘
          │ REST API Calls
          ▼
┌──────────────────────────────────────────┐
│              Spring Boot Application     │
│                                          │
│  ┌───────────────┐   ┌─────────────────┐ │
│  │   CRM API     │   │     ERP API     │ │
│  │ (/api/crm)    │   │   (/api/erp)    │ │
│  │               │   │                 │ │
│  │ - Customers   │   │ - Pull Orders   │ │
│  │ - Orders      │   │ - Acknowledge   │ │
│  │               │   │ - Update Status │ │
│  └───────┬───────┘   └────────┬────────┘ │
│          │                    │          │
│          └──────────┬─────────┘          │
│                     ▼                    │
│          ┌───────────────────────────┐   │
│          │     Integration API       │   │
│          │ (/api/integrations)       │   │
│          │                           │   │
│          │ - Bulk Import             │   │
│          │ - Webhooks                │   │
│          │ - CSV Export              │   │
│          │ - Health & Metrics        │   │
│          └───────────┬───────────────┘   │
│                      ▼                   │
│        ┌────────────────────────────┐    │
│        │ Service Layer              │    │
│        │ - Business Logic           │    │
│        │ - Validation               │    │
│        │ - Order Lifecycle          │    │
│        └───────────┬────────────────┘    │
│                    ▼                     │
│        ┌────────────────────────────┐    │
│        │ Repository Layer (JPA)     │    │
│        │ - CustomerRepository       │    │
│        │ - OrderRepository          │    │
│        │ - ProductRepository        │    │
│        └───────────┬────────────────┘    │
│                    ▼                     │
│        ┌────────────────────────────┐    │
│        │ MySQL Database             │    │
│        │ crm_erp_sync_db            │    │
│        └────────────────────────────┘    │
└──────────────────────────────────────────┘
```

### Architecture Explanation

- **Controller Layer**
  - Exposes REST APIs grouped by business responsibility (CRM, ERP, Integration)
- **Service Layer**
  - Contains business logic, validations, and lifecycle rules
- **Repository Layer**
  - Handles database access using Spring Data JPA
- **Database Layer**
  - MySQL database (`crm_erp_sync_db`) persists all data

---

## 6. API Endpoints

### 6.1 CRM API (`/api/crm`)

#### Customers

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/api/crm/customer` | Create customer |
| PUT | `/api/crm/customer/{id}` | Update customer |
| GET | `/api/crm/customer` | List customers (pagination) |

#### Orders

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/api/crm/order` | Create order |
| GET | `/api/crm/order` | List orders (DTO, pagination, filters) |

---

### 6.2 ERP API (`/api/erp`)

| Method | Endpoint | Description |
|------|--------|------------|
| GET | `/api/erp/orders/pull` | Pull NEW orders |
| POST | `/api/erp/orders/{id}/ack` | Acknowledge order |
| GET | `/api/erp/orders` | List orders |
| GET | `/api/erp/orders/{id}` | Get order details |
| PUT | `/api/erp/orders/{id}/status` | Update order status |
| GET | `/api/erp/customers/{id}` | Get customer |

---

### 6.3 Integration API (`/api/integrations`)

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/api/integrations/import/customers` | Bulk customer import |
| POST | `/api/integrations/webhooks/erp-order-status` | ERP webhook |
| GET | `/api/integrations/export/orders.csv` | CSV export |
| GET | `/api/integrations/health` | Health check |
| GET | `/api/integrations/metrics` | System metrics |

---

## 7. Key Features

- RESTful API design with proper HTTP status codes
- DTO-based responses to avoid entity exposure
- Pagination and filtering support
- Order lifecycle management
- ERP pull-and-acknowledge integration pattern
- Webhook-based asynchronous status updates
- CSV export for reporting and BI tools
- Centralized exception handling

## 8. Setup and Installation

### Prerequisites

- Java 21 (LTS)
- Maven 3.x
- MySQL 8.x
- Postman

---

### Steps to Run the Application

#### 1. Clone the repository

```bash
git clone https://github.com/agthuhein/crm-erp-integration-api.git
cd crm-erp-integration-api
```
#### 2. Set up the MySQL Database

1. Start the MySQL server

2. Create a new database:

```sql
CREATE DATABASE crm_erp_sync_db;
```
3. Go to Database Backup folder. Then get the MySQL Database backup file
4. Open command line/terminal. Change directory to where the backup file exits
5. Backup the crm_erp_sync_db_BK.sql using the command below

```sql
mysql -u root -p crm_erp_sync_db < crm_erp_sync_db_BK.sql
```
6. Configure the connection in application.yml file


#### 3. Configure the database connection

Open src/main/resources/application.yml and configure:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/crm_erp_sync
    username: root
    password: your_password

```
Database tables are created automatically on application startup using Hibernate (ddl-auto=update).

#### 4. Run the application
From the project root directory:
```bash
mvn clean spring-boot:run
```

The application will be available at:
```arduino
http://localhost:8080
```

### 9. Steps to Test the Application
Install Postman API Client
Download Postman from:
```arduino
https://www.postman.com/downloads/
```
### Recommended API Testing Flow

1. **Create Customer**
- `POST /api/crm/customer`

2. **Create Order**
- `POST /api/crm/order`

3. **ERP Pull New Orders**
- `GET /api/erp/orders/pull`

4. **ERP Acknowledge Order**
- `POST /api/erp/orders/{id}/ack`

5. **Update Order Status**
- `PUT /api/erp/orders/{id}/status`

6. **View Metrics**
- `GET /api/integrations/metrics`

7. **Export Orders as CSV**
- `GET /api/integrations/export/orders.csv?from=YYYY-MM-DDTHH:mm:ssZ&to=YYYY-MM-DDTHH:mm:ssZ`

### 10. Testing Summary

All APIs were tested using **Postman** to verify correctness, data integrity, and integration workflows.

Test cases include:

- Customer creation and update
- Order creation
- ERP pull and acknowledge workflow
- Order status transitions
- Bulk customer import
- Metrics retrieval
- CSV export for reporting

---

### 11. Conclusion

This project will show how to use REST API for integrating enterprise systems, ensuring data consistency; using REST API also supports the principle of Separation Between Use Cases and therefore allows for the development of a scalable and secure REST API interface to perform integration between two separate business applications (CRM, ERP).

Additionally, the solution is intended to meet the requirement of creating secure and scalable REST APIs for integrating enterprise business applications (CRM and ERP business applications) using appropriate real-time enterprise integration patterns.