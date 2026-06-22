# SmartStudent – Student Management System

## Overview

SmartStudent is a Java-based Student Management System designed to simplify the management of student records through a secure and efficient platform. The application allows administrators to perform CRUD (Create, Read, Update, Delete) operations, search student records, and manage data stored in a MySQL database using JDBC.

This project demonstrates the practical implementation of Object-Oriented Programming (OOP), database connectivity, SQL operations, and software development best practices.

## Features

### Admin Authentication

* Secure admin login system
* Access restriction for unauthorized users

### Student Management

* Add new student records
* View all student records
* Update student information
* Delete student records

### Search & Filter

* Search by Roll Number
* Search by Department
* Search by Marks

### Database Integration

* MySQL database connectivity using JDBC
* Persistent data storage and retrieval

### Validation

* Email validation
* Phone number validation
* Marks validation

---

## Technology Stack

* Java
* JDBC
* MySQL
* Maven
* VS Code
* Git & GitHub

---

## Project Structure

```text
SmartStudent/
│
├── src/
│   ├── model/
│   ├── dao/
│   ├── service/
│   ├── util/
│   ├── ui/
│   └── Main.java
│
├── database/
│   └── student.sql
│
├── README.md
└── pom.xml
```

---

## Database Schema

### Students Table

| Column     | Data Type |
| ---------- | --------- |
| id         | INT       |
| roll_no    | VARCHAR   |
| name       | VARCHAR   |
| department | VARCHAR   |
| email      | VARCHAR   |
| phone      | VARCHAR   |
| marks      | DOUBLE    |

---

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
```

### 2. Create Database

```sql
CREATE DATABASE smartstudent;
```

### 3. Import SQL Schema

Run the SQL script provided in:

```text
database/student.sql
```

### 4. Configure Database Credentials

Update the database configuration in:

```java
DBConnection.java
```

```java
private static final String URL = "jdbc:mysql://localhost:3306/smartstudent";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

### 5. Run the Application

Compile and execute the project using Maven or your preferred Java IDE.

---

## Learning Outcomes

This project helped in understanding and implementing:

* Object-Oriented Programming (OOP)
* JDBC Connectivity
* MySQL Database Management
* CRUD Operations
* Exception Handling
* Input Validation
* Software Design Principles
* Version Control with Git and GitHub

---

## Future Enhancements

* Java Swing GUI
* Password Encryption
* CSV Export Functionality
* Dashboard Analytics
* Role-Based Access Control
* Cloud Database Integration

---

## Author

Rishabh Mishra


Internship Project – SmartStudent Student Management System
