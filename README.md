# Library Management System

A desktop application built with Java, Swing, JDBC, and MySQL/MariaDB for efficient library book management.

## Overview

This Library Management System provides an intuitive interface for librarians to manage books with full CRUD operations. The application features a clean Swing GUI with database integration, allowing users to add, update, delete, and search books efficiently.

## Current Features ✅

- **Book Management**: Add, update, delete, and search books with title, author, publisher, and year
- **Database Integration**: Secure MySQL/MariaDB database connection using JDBC
- **User-Friendly Interface**: Clean Java Swing GUI with intuitive navigation
- **Data Persistence**: All book records are stored in the database
- **Search Functionality**: Search books by title with real-time filtering
- **Error Handling**: Robust error handling with user-friendly messages
- **Table View**: Display all books in an organized table format
- **Form Validation**: Input validation for data integrity

## Technologies Used

- Java 8+
- Swing GUI Framework
- JDBC for database connectivity
- MySQL/MariaDB Database
- Design Patterns: MVC Architecture

## Getting Started

### Prerequisites
- Java JDK 8 or higher
- XAMPP (includes MySQL/MariaDB server)
- MySQL Connector/J JDBC Driver

### Setup Instructions

1. **Install and Start XAMPP**
   - Download XAMPP from https://www.apachefriends.org/
   - Install and start the MySQL service in XAMPP Control Panel

2. **Download MySQL JDBC Driver**
   - Go to https://dev.mysql.com/downloads/connector/j/
   - Download "Platform Independent" version (.zip)
   - Extract and copy `mysql-connector-j-x.x.x.jar` to your project `src/` folder

3. **Setup Database**
   ```bash
   # Open XAMPP shell or command prompt
   mysql -u root -p
   # Press Enter for password (XAMPP default has no password)
   
   # Create database and table
   CREATE DATABASE librarydb;
   USE librarydb;
   CREATE TABLE books (
       id INT PRIMARY KEY AUTO_INCREMENT,
       title VARCHAR(255) NOT NULL,
       author VARCHAR(255) NOT NULL,
       publisher VARCHAR(255),
       year INT,
       available BOOLEAN DEFAULT TRUE
   );
   exit
   ```

4. **Update Database Credentials**
   - Edit `src/DBConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/librarydb";
   private static final String USER = "root";
   private static final String PASSWORD = "";  // Empty for XAMPP default
   ```

5. **Compile and Run**
   ```bash
   cd src
   javac -cp "mysql-connector-j-x.x.x.jar;." *.java
   java -cp "mysql-connector-j-x.x.x.jar;." LibraryManagementSystem
   ```

## Project Structure

- `src/Book.java`: Book model class with enhanced metadata support
- `src/Member.java`: Member model class (foundation for future features)
- `src/BorrowRecord.java`: Borrowing system model (foundation for future features)
- `src/DBConnection.java`: Simple database connection handler
- `src/DatabaseManager.java`: Advanced database operations with transaction support
- `src/Logger.java`: Comprehensive logging system
- `src/ValidationUtils.java`: Input validation utilities
- `src/LibraryManagementSystem.java`: Main application with Swing GUI

## How to Use

1. **Launch the application** - A GUI window will open
2. **Add Books**: Fill in the form and click "Add Book"
3. **View Books**: All books display in the table automatically
4. **Update Books**: Click on a table row to select, modify fields, and click "Update Book"
5. **Delete Books**: Select a book and click "Delete Book"
6. **Search Books**: Enter a title in the search field and click "Search"
7. **Refresh View**: Click "Refresh" to reload all books

## Planned Enhancements 🚀

- Member management integration
- Book borrowing and return system
- Fine calculation and tracking
- Copy management for multiple book copies
- Advanced search and filtering
- Barcode scanning integration
- Email notifications for due dates
- Reporting capabilities

## Troubleshooting

### Common Issues:
- **"No suitable driver found"**: Ensure MySQL connector jar is in classpath
- **"Unknown database"**: Create the database using the setup instructions
- **"Table doesn't exist"**: Create the books table using the SQL provided
- **Connection refused**: Make sure XAMPP MySQL service is running

## Development Timeline

**Completed (December 2024):**
- ✅ Core book management system
- ✅ Database integration
- ✅ Basic GUI implementation
- ✅ CRUD operations

**In Progress (January 2025):**
- 🔄 Member management integration
- 🔄 Borrowing system implementation

**Planned (February-March 2025):**
- 📋 Advanced reporting
- 📋 Enhanced UI/UX
- 📋 Additional features

## Contact

For any questions or suggestions, please open an issue in this repository.

---

**Note**: This is a working desktop application suitable for basic library book management. The foundation is solid and ready for additional feature integration.
