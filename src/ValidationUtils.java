import java.util.regex.Pattern;
import java.time.Year;

public class ValidationUtils {
    private static final Logger logger = Logger.getInstance();
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    // Phone validation pattern (accepts formats like XXX-XXX-XXXX, (XXX) XXX-XXXX, etc.)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$");
    
    // ISBN validation pattern (ISBN-10 or ISBN-13)
    private static final Pattern ISBN_PATTERN = 
        Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    
    // Book validation
    public static boolean validateBook(Book book) {
        try {
            // Title validation
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                logger.error("Book validation failed: Title cannot be empty");
                return false;
            }
            
            // Author validation
            if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
                logger.error("Book validation failed: Author cannot be empty");
                return false;
            }
            
            // Year validation
            int currentYear = Year.now().getValue();
            if (book.getYear() <= 0 || book.getYear() > currentYear) {
                logger.error("Book validation failed: Invalid year - " + book.getYear());
                return false;
            }
            
            // ISBN validation (if provided)
            String isbn = book.getIsbn();
            if (isbn != null && !isbn.trim().isEmpty() && !ISBN_PATTERN.matcher(isbn).matches()) {
                logger.error("Book validation failed: Invalid ISBN format - " + isbn);
                return false;
            }
            
            // Copies validation
            if (book.getTotalCopies() <= 0) {
                logger.error("Book validation failed: Total copies must be positive");
                return false;
            }
            
            if (book.getAvailableCopies() < 0 || book.getAvailableCopies() > book.getTotalCopies()) {
                logger.error("Book validation failed: Available copies must be between 0 and total copies");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Book validation error", e);
            return false;
        }
    }
    
    // Member validation
    public static boolean validateMember(Member member) {
        try {
            // Name validation
            if (member.getName() == null || member.getName().trim().isEmpty()) {
                logger.error("Member validation failed: Name cannot be empty");
                return false;
            }
            
            // Email validation
            String email = member.getEmail();
            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                logger.error("Member validation failed: Invalid email format - " + email);
                return false;
            }
            
            // Phone validation (if provided)
            String phone = member.getPhone();
            if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
                logger.error("Member validation failed: Invalid phone format - " + phone);
                return false;
            }
            
            // Max books allowed validation
            if (member.getMaxBooksAllowed() <= 0) {
                logger.error("Member validation failed: Max books allowed must be positive");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            logger.error("Member validation error", e);
            return false;
        }
    }
    
    // Input string sanitization to prevent SQL injection
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Remove potentially dangerous characters
        return input.replaceAll("[;'\"]", "");
    }
    
    // Integer validation
    public static boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Double validation
    public static boolean isValidDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Date validation in format yyyy-MM-dd
    public static boolean isValidDate(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }
    
    // Check if a member can borrow books
    public static boolean canMemberBorrow(Member member, DatabaseManager dbManager) {
        // Check if member is active
        if (!member.isActive()) {
            logger.info("Member cannot borrow: Inactive membership - " + member.getName());
            return false;
        }
        
        // Check if member has fines
        if (member.getFineAmount() > 0) {
            logger.info("Member cannot borrow: Has unpaid fines - " + member.getName());
            return false;
        }
        
        // Check if member has reached maximum books limit
        if (member.getCurrentBorrowCount() >= member.getMaxBooksAllowed()) {
            logger.info("Member cannot borrow: Maximum books limit reached - " + member.getName());
            return false;
        }
        
        return true;
    }
    
    // Check if a book can be borrowed
    public static boolean canBookBeBorrowed(Book book) {
        if (!book.isAvailable() || book.getAvailableCopies() <= 0) {
            logger.info("Book cannot be borrowed: Not available - " + book.getTitle());
            return false;
        }
        return true;
    }
} 