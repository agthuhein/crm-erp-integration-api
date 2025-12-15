# CRM ↔ ERP Customer and Order Synchronization REST API

## 1. Introduction

This project implements a **RESTful API** to integrate **CRM and ERP business systems**.  
The system focuses on synchronizing **customer and order data**, enabling reliable data exchange, process automation, and reporting across enterprise applications.

The solution is built using **Spring Boot**, **REST APIs**, and **MySQL**, following **clean architecture** and **enterprise integration best practices**.

---

## 2. Objectives

- Design secure and scalable REST APIs
- Enable CRM-to-ERP order synchronization
- Prevent duplicate data processing
- Support reporting and system monitoring
- Demonstrate real-world enterprise integration patterns

---

## 3. Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- RESTful APIs
- Postman (API testing)
- CSV (data export format)

---

## 4. System Overview

The system is divided into **three logical components**:

### CRM API
- Manages customer data
- Handles order creation and listing

### ERP API
- Pulls new orders from CRM
- Updates order processing status

### Integration API
- Provides bulk import
- Handles ERP webhooks
- Exports data in CSV format
- Offers health checks and metrics for monitoring

---

## 5. API Modules

### CRM APIs
- Create customer
- Update customer
- Create order
- List orders (DTO-based, paginated)

### ERP APIs
- Pull new orders
- Acknowledge imported orders
- Update order status
- Retrieve customer information

### Integration APIs
- Bulk customer import
- ERP webhook for order status updates
- CSV export for reporting
- System health endpoint
- Metrics endpoint

---

## 6. Key Features

- RESTful design with proper HTTP status codes
- DTO-based responses to avoid entity exposure
- Pagination and filtering support
- Order lifecycle management
- ERP pull-and-acknowledge integration pattern
- CSV export for BI and reporting systems
- Centralized exception handling

---

## 7. Testing

All APIs were tested using **Postman**.

Test scenarios include:
- Order creation
- ERP pull and acknowledge flow
- Order status transitions
- Bulk customer import
- Metrics retrieval
- CSV export

---

## 8. Conclusion

This project demonstrates how **REST APIs** can be used to integrate enterprise systems efficiently, ensuring **data consistency**, **scalability**, and **interoperability** between CRM and ERP platforms.
