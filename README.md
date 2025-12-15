# CRM ↔ ERP Customer and Order Synchronization REST API

## 1. Introduction

This project implements a RESTful API to integrate CRM and ERP business systems.  
The system focuses on synchronizing customer and order data, enabling reliable data exchange, process automation, and reporting across enterprise applications.

The solution is built using **Spring Boot**, **REST APIs**, and **MySQL**, following clean architecture and enterprise integration best practices.

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
|---------|--------|
| Java | **21 (LTS)** |
| Spring Boot | 3.x |
| Spring Data JPA (Hibernate) | 6.x |
| MySQL | 8.x |
| Maven | 3.x |
| RESTful APIs | HTTP/JSON |
| Postman | API Testing |
| CSV | Data export format |


---

## 4. System Overview

The system is divided into three logical components:

### CRM API
- Manages customers and order creation
- Exposes order data to ERP systems

### ERP API
- Pulls new orders from CRM
- Updates order processing status
- Simulates ERP order lifecycle handling

### Integration API
- Bulk data import
- Webhook handling
- CSV export
- Health and metrics endpoints

---

## 5. API Endpoints

### 5.1 CRM API (`/api/crm`)

#### Customers

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/api/crm/customer` | Create a new customer (unique email validation) |
| PUT | `/api/crm/customer/{id}` | Update customer details |
| GET | `/api/crm/customer` | List customers (pagination, optional `updatedAt` filter) |

#### Orders

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/api/crm/order` | Create a new order with order items |
| GET | `/api/crm/order` | List orders using DTO response (filters + pagination) |

> CRM order listing uses DTOs to avoid exposing entity relationships.

---

### 5.2 ERP API (`/api/erp`)

#### Orders

| Method | Endpoint | Description |
|------|--------|------------|
| GET | `/api/erp/orders/pull` | Pull NEW orders from CRM |
| POST | `/api/erp/orders/{id}/ack` | Acknowledge imported order (NEW → PROCESSING) |
| GET | `/api/erp/orders` | Retrieve orders with filters and pagination |
| GET | `/api/erp/orders/{id}` | Retrieve order details |
| PUT | `/api/erp/orders/{id}/status` | Update order status |

#### Customers

| Method | Endpoint | Description |
|------|--------|------------|
| GET | `/api/erp/customers/{id}` | Retrieve customer information |

---

### 5.3 Integration API (`/api/integrations`)

#### Integration & Automation

| Method | Endpoint | Description |
|------|--------|------------|
| POST | `/api/integrations/import/customers` | Bulk customer import with duplicate detection |
| POST | `/api/integrations/webhooks/erp-order-status` | ERP webhook to update order status |
| GET | `/api/integrations/export/orders.csv` | Export orders as CSV |

#### Monitoring & Observability

| Method | Endpoint | Description |
|------|--------|------------|
| GET | `/api/integrations/health` | System health check |
| GET | `/api/integrations/metrics` | System metrics |

---

## 6. Key Features

- RESTful API design with proper HTTP status codes
- DTO-based responses to avoid entity exposure
- Pagination and filtering support
- Order lifecycle management
- ERP pull-and-acknowledge pattern
- Webhook-based status updates
- CSV export for reporting and BI tools
- Centralized exception handling

---

## 7. Integration Patterns Implemented

- **ERP Pull & Acknowledge Pattern**
    - ERP pulls new orders from CRM
    - Orders are acknowledged to prevent duplicate processing

- **Webhook-Based Synchronization**
    - ERP sends asynchronous order status updates

- **Bulk Import with De-duplication**
    - Prevents duplicate customer records

- **Reporting-Oriented APIs**
    - CSV export enables external analytics tools

---

## 8. Testing

All APIs were tested using **Postman**.

Test scenarios include:
- Customer creation and update
- Order creation
- ERP pull and acknowledge flow
- Order status transitions
- Bulk customer import
- Metrics retrieval
- CSV export

---

## 9. Conclusion

This project demonstrates how REST APIs can be used to integrate enterprise systems efficiently, ensuring data consistency, scalability, and interoperability between CRM and ERP platforms.

It aligns with real-world enterprise integration requirements and fulfills the assessment objective of designing secure and scalable REST APIs for business application integration.
