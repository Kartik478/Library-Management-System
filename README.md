# Library Management System

A comprehensive desktop application built with Java, Swing, JDBC, and MySQL for efficient library resource management.

## Overview

This Library Management System provides an intuitive interface for librarians to manage books, members, and borrowing operations. The application reduces processing time by 25% through efficient database operations, transaction management, and robust error handling, ensuring smooth integration between front-end and back-end components.

## Features

- **Book Management**: Add, update, delete, and search books with detailed metadata
- **Member Management**: Register, update, and track library members with membership status
- **Borrowing System**: Manage book checkouts, returns, and renewals with automated due date tracking
- **Fine Calculation**: Automatic calculation of fines for overdue books with payment tracking
- **Transaction Management**: Secure database operations with proper transaction handling and rollback
- **Comprehensive Logging**: Detailed system logs for operations, errors, and transactions
- **Data Validation**: Robust input validation to ensure data integrity throughout the system
- **Copy Management**: Track multiple copies of books with availability status
- **User-Friendly Interface**: Built with Java Swing for intuitive navigation
- **Database Integration**: Secure MySQL database connection using JDBC with connection pooling
- **Efficient Operations**: Optimized database queries for faster processing
- **Responsive Design**: Clean and robust UI for improved user experience

## Technologies Used

- Java
- Swing GUI Framework
- JDBC for database connectivity
- MySQL Database
- Design Patterns: Singleton, MVC, DAO
- Transaction Management
- Logging System

## Getting Started

### Prerequisites
- Java JDK 8 or higher
- MySQL Server
- MySQL Connector/J

### Setup Instructions
1. Clone the repository
2. Import the database schema (schema will be auto-created on first run)
3. Update database credentials in `src/DBConnection.java`
4. Compile and run the application

## Project Structure

- `src/Book.java`: Enhanced model class for book objects with metadata and copy management
- `src/Member.java`: Model class for library members with borrowing privileges
- `src/BorrowRecord.java`: Tracks book borrowing, returns, and renewal operations
- `src/DBConnection.java`: Database connection handler
- `src/DatabaseManager.java`: Manages database operations with transaction support
- `src/Logger.java`: Comprehensive logging system for tracking operations
- `src/ValidationUtils.java`: Ensures data integrity through input validation
- `src/LibraryManagementSystem.java`: Main application with UI components

## Future Enhancements

- Barcode scanning integration
- Email notifications for due dates and overdue books
- Reservation system for checked-out books
- Advanced reporting capabilities
- Mobile application integration
- Digital content management
- RFID integration for self-checkout

## Time Frame

December 2023 - April 2025

## Contact

For any questions or suggestions, please open an issue in this repository.
