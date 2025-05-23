import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final Logger logger = Logger.getInstance();
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            createTables(conn);
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
    
    private void createTables(Connection conn) throws SQLException {
        String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "title VARCHAR(255) NOT NULL, " +
                "author VARCHAR(255) NOT NULL, " +
                "publisher VARCHAR(255), " +
                "year INT, " +
                "isbn VARCHAR(20), " +
                "category VARCHAR(100), " +
                "total_copies INT DEFAULT 1, " +
                "available_copies INT DEFAULT 1, " +
                "location VARCHAR(100), " +
                "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "available BOOLEAN DEFAULT TRUE)";
        
        String createMembersTable = "CREATE TABLE IF NOT EXISTS members (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) UNIQUE NOT NULL, " +
                "phone VARCHAR(20), " +
                "address VARCHAR(255), " +
                "join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "expiry_date TIMESTAMP, " +
                "active BOOLEAN DEFAULT TRUE, " +
                "member_type VARCHAR(50) DEFAULT 'Regular', " +
                "max_books_allowed INT DEFAULT 3, " +
                "fine_amount DOUBLE DEFAULT 0.0)";
        
        String createBorrowRecordsTable = "CREATE TABLE IF NOT EXISTS borrow_records (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "member_id INT NOT NULL, " +
                "book_id INT NOT NULL, " +
                "borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "due_date TIMESTAMP NOT NULL, " +
                "return_date TIMESTAMP, " +
                "fine_amount DOUBLE DEFAULT 0.0, " +
                "renewed BOOLEAN DEFAULT FALSE, " +
                "renewal_count INT DEFAULT 0, " +
                "FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE)";
        
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(createBooksTable);
        stmt.executeUpdate(createMembersTable);
        stmt.executeUpdate(createBorrowRecordsTable);
        stmt.close();
    }
    
    // Book CRUD Operations
    public int addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, year, isbn, category, total_copies, available_copies, location, available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getYear());
            pstmt.setString(5, book.getIsbn());
            pstmt.setString(6, book.getCategory());
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            pstmt.setString(9, book.getLocation());
            pstmt.setBoolean(10, book.isAvailable());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    logger.info("Book added successfully: " + book.getTitle() + " (ID: " + id + ")");
                    return id;
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding book: " + book.getTitle(), e);
            return -1;
        }
    }
    
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, " +
                "isbn = ?, category = ?, total_copies = ?, available_copies = ?, " +
                "location = ?, available = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getYear());
            pstmt.setString(5, book.getIsbn());
            pstmt.setString(6, book.getCategory());
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            pstmt.setString(9, book.getLocation());
            pstmt.setBoolean(10, book.isAvailable());
            pstmt.setInt(11, book.getId());
            
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Book updated successfully: " + book.getTitle() + " (ID: " + book.getId() + ")");
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error updating book: " + book.getTitle(), e);
            return false;
        }
    }
    
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Book deleted successfully: ID " + bookId);
                return true;
            }
            return false;
        } catch (SQLException e) {
            logger.error("Error deleting book with ID: " + bookId, e);
            return false;
        }
    }
    
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getInt("year"),
                    rs.getBoolean("available"),
                    rs.getString("isbn"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getString("location")
                );
                return book;
            }
        } catch (SQLException e) {
            logger.error("Error retrieving book with ID: " + bookId, e);
        }
        return null;
    }
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getInt("year"),
                    rs.getBoolean("available"),
                    rs.getString("isbn"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getString("location")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all books", e);
        }
        
        return books;
    }
    
    public List<Book> searchBooks(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR publisher LIKE ? OR isbn LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String term = "%" + searchTerm + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);
            pstmt.setString(3, term);
            pstmt.setString(4, term);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getInt("year"),
                    rs.getBoolean("available"),
                    rs.getString("isbn"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getString("location")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            logger.error("Error searching for books with term: " + searchTerm, e);
        }
        
        return books;
    }
    
    // Member CRUD operations
    public int addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone, address, expiry_date, active, member_type, max_books_allowed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getAddress());
            pstmt.setTimestamp(5, new Timestamp(member.getExpiryDate().getTime()));
            pstmt.setBoolean(6, member.isActive());
            pstmt.setString(7, member.getMemberType());
            pstmt.setInt(8, member.getMaxBooksAllowed());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating member failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    logger.info("Member added successfully: " + member.getName() + " (ID: " + id + ")");
                    return id;
                } else {
                    throw new SQLException("Creating member failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding member: " + member.getName(), e);
            return -1;
        }
    }
    
    // Transaction operations
    public boolean borrowBook(Member member, Book book) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Update book availability
            String updateBookSql = "UPDATE books SET available_copies = available_copies - 1, " +
                    "available = CASE WHEN available_copies - 1 > 0 THEN TRUE ELSE FALSE END " +
                    "WHERE id = ? AND available_copies > 0";
            
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
            updateBookStmt.setInt(1, book.getId());
            int bookRowsAffected = updateBookStmt.executeUpdate();
            
            if (bookRowsAffected == 0) {
                conn.rollback();
                logger.error("Failed to borrow book: " + book.getTitle() + " - No available copies");
                return false;
            }
            
            // Create borrow record
            String borrowSql = "INSERT INTO borrow_records (member_id, book_id, due_date) " +
                    "VALUES (?, ?, ?)";
            
            // Calculate due date (14 days from now)
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 14);
            Timestamp dueDate = new Timestamp(calendar.getTimeInMillis());
            
            PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
            borrowStmt.setInt(1, member.getId());
            borrowStmt.setInt(2, book.getId());
            borrowStmt.setTimestamp(3, dueDate);
            
            int borrowRowsAffected = borrowStmt.executeUpdate();
            
            if (borrowRowsAffected > 0) {
                conn.commit();
                logger.info("Book borrowed successfully: " + book.getTitle() + " by " + member.getName());
                return true;
            } else {
                conn.rollback();
                logger.error("Failed to create borrow record for book: " + book.getTitle());
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
            logger.error("Error borrowing book: " + book.getTitle(), e);
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
    
    public boolean returnBook(int borrowId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get borrow record
            String getBorrowSql = "SELECT * FROM borrow_records WHERE id = ? AND return_date IS NULL";
            PreparedStatement getBorrowStmt = conn.prepareStatement(getBorrowSql);
            getBorrowStmt.setInt(1, borrowId);
            ResultSet rs = getBorrowStmt.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                logger.error("Borrow record not found or book already returned: " + borrowId);
                return false;
            }
            
            int bookId = rs.getInt("book_id");
            int memberId = rs.getInt("member_id");
            
            // Update book availability
            String updateBookSql = "UPDATE books SET available_copies = available_copies + 1, available = TRUE WHERE id = ?";
            PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
            updateBookStmt.setInt(1, bookId);
            updateBookStmt.executeUpdate();
            
            // Calculate fine if overdue
            Timestamp dueDate = rs.getTimestamp("due_date");
            Timestamp now = new Timestamp(System.currentTimeMillis());
            double fineAmount = 0.0;
            
            if (now.after(dueDate)) {
                long diffInMillies = now.getTime() - dueDate.getTime();
                long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
                fineAmount = diffInDays * 1.0; // $1 per day
            }
            
            // Mark book as returned and record fine
            String returnSql = "UPDATE borrow_records SET return_date = ?, fine_amount = ? WHERE id = ?";
            PreparedStatement returnStmt = conn.prepareStatement(returnSql);
            returnStmt.setTimestamp(1, now);
            returnStmt.setDouble(2, fineAmount);
            returnStmt.setInt(3, borrowId);
            returnStmt.executeUpdate();
            
            // Update member's fine amount if needed
            if (fineAmount > 0) {
                String updateMemberSql = "UPDATE members SET fine_amount = fine_amount + ? WHERE id = ?";
                PreparedStatement updateMemberStmt = conn.prepareStatement(updateMemberSql);
                updateMemberStmt.setDouble(1, fineAmount);
                updateMemberStmt.setInt(2, memberId);
                updateMemberStmt.executeUpdate();
            }
            
            conn.commit();
            logger.info("Book returned successfully with borrow ID: " + borrowId + (fineAmount > 0 ? " with fine: $" + fineAmount : ""));
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
            logger.error("Error returning book with borrow ID: " + borrowId, e);
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
} 