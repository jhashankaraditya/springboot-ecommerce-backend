# Spring Boot E-Commerce Backend

A backend-only E-Commerce application built using Java, Spring Boot, Spring Security, JWT Authentication, JPA/Hibernate, MySQL, Swagger/OpenAPI, and JUnit/Mockito.

## Features

### Authentication & Authorization

* User Registration
* User Login
* JWT Authentication
* Role-Based Access Control
* Protected APIs

### Category Management

* Create Category
* Update Category
* Delete Category
* View Categories

### Product Management

* Create Product
* Update Product
* Delete Product
* View Products

### Cart Management

* Add Product to Cart
* Update Quantity
* Remove Product from Cart
* View Cart

### Order Management

* Place Order
* View My Orders
* View Order by ID
* Cancel Order
* Update Order Status
* View All Orders (Admin)

## Tech Stack

* Java 21
* Spring Boot
* Spring Security
* JWT
* Hibernate / JPA
* MySQL
* Swagger / OpenAPI
* Maven
* JUnit 5
* Mockito

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Project Structure

src/main/java

* controller
* service
* repository
* dto
* model
* security
* exception
* config

## Testing

Unit testing implemented using:

* JUnit 5
* Mockito

Tested service-layer business logic including:

* Order placement
* Order cancellation
* Ownership validation
* Exception handling
* Repository failure scenarios

## Future Enhancements

* Docker
* CI/CD Pipeline
* Redis Caching
* Payment Gateway Integration
* Frontend Application
* Cloud Deployment

## Author

Aditya Shankar Jha
