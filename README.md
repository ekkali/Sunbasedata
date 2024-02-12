# SunbaseData

## Overview

This project is a web application that manages customer data. It provides functionalities to view, add, update, and
delete customer records. The project also includes user authentication.

## Technologies Used

- **Spring Boot:**
- **Spring Security:**
- **Spring Data JPA:**
- **JWT (JSON Web Token):** Token-based authentication for securing APIs.
- **MySql DataBase:**
- **Front-end:** JSP, CSS, JavaScript,Bootstrap,JQuery.

## API
       public ==>     /login,/signup
       private ===>   /api/customer/**

### Database Configuration

```properties
#  MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update


