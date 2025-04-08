import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_DIRECTORY = "logs";
    private static final String ERROR_LOG_FILE = LOG_DIRECTORY + "/error.log";
    private static final String INFO_LOG_FILE = LOG_DIRECTORY + "/info.log";
    private static final String DEBUG_LOG_FILE = LOG_DIRECTORY + "/debug.log";
    private static final String TRANSACTION_LOG_FILE = LOG_DIRECTORY + "/transaction.log";
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static Logger instance;
    
    private Logger() {
        // Create log directory if it doesn't exist
        File logDir = new File(LOG_DIRECTORY);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }
    
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    public void error(String message) {
        log(ERROR_LOG_FILE, "[ERROR] " + message);
        System.err.println("[ERROR] " + message);
    }
    
    public void error(String message, Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ERROR] ").append(message).append("\n");
        sb.append(throwable.getMessage()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\t").append(element.toString()).append("\n");
        }
        log(ERROR_LOG_FILE, sb.toString());
        System.err.println("[ERROR] " + message);
        throwable.printStackTrace(System.err);
    }
    
    public void info(String message) {
        log(INFO_LOG_FILE, "[INFO] " + message);
        System.out.println("[INFO] " + message);
    }
    
    public void debug(String message) {
        log(DEBUG_LOG_FILE, "[DEBUG] " + message);
    }
    
    public void transaction(String message) {
        log(TRANSACTION_LOG_FILE, "[TRANSACTION] " + message);
    }
    
    public void bookAction(String action, Book book, Member member) {
        String message = String.format("%s - Book ID: %d, Title: %s, Member: %s",
                action, book.getId(), book.getTitle(), member != null ? member.getName() : "N/A");
        transaction(message);
    }
    
    public void memberAction(String action, Member member) {
        String message = String.format("%s - Member ID: %d, Name: %s", 
                action, member.getId(), member.getName());
        transaction(message);
    }
    
    private synchronized void log(String fileName, String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(DATE_FORMAT.format(new Date()) + " " + message);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + fileName);
            e.printStackTrace();
        }
    }
    
    public void clearLogs() {
        clearLog(ERROR_LOG_FILE);
        clearLog(INFO_LOG_FILE);
        clearLog(DEBUG_LOG_FILE);
    }
    
    private void clearLog(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, false))) {
            writer.println(DATE_FORMAT.format(new Date()) + " [SYSTEM] Log cleared");
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + fileName);
            e.printStackTrace();
        }
    }
} 