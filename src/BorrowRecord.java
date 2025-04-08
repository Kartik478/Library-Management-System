import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BorrowRecord {
    private int id;
    private Member member;
    private Book book;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    private boolean renewed;
    private int renewalCount;
    private static final int MAX_RENEWAL_COUNT = 2;
    private static final int BORROW_PERIOD_DAYS = 14;

    public BorrowRecord(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.borrowDate = new Date();
        this.dueDate = calculateDueDate();
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.renewed = false;
        this.renewalCount = 0;
    }

    public BorrowRecord(int id, Member member, Book book, Date borrowDate) {
        this.id = id;
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = calculateDueDate(borrowDate);
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.renewed = false;
        this.renewalCount = 0;
    }

    private Date calculateDueDate() {
        return calculateDueDate(this.borrowDate);
    }

    private Date calculateDueDate(Date fromDate) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(java.util.Calendar.DAY_OF_MONTH, BORROW_PERIOD_DAYS);
        return calendar.getTime();
    }

    public void returnBook() {
        this.returnDate = new Date();
        this.fineAmount = calculateFine();
    }

    public double calculateFine() {
        if (returnDate == null) {
            return 0.0;
        }
        
        long daysOverdue = getDaysOverdue();
        if (daysOverdue <= 0) {
            return 0.0;
        }
        
        double fineRate = 1.0; // $1 per day
        return daysOverdue * fineRate;
    }

    public boolean renewBook() {
        if (renewed || renewalCount >= MAX_RENEWAL_COUNT || returnDate != null) {
            return false;
        }
        
        // Check if book is already overdue
        if (new Date().after(dueDate)) {
            return false;
        }
        
        this.dueDate = calculateDueDate(new Date());
        this.renewed = true;
        this.renewalCount++;
        return true;
    }

    public long getDaysOverdue() {
        Date checkDate = returnDate != null ? returnDate : new Date();
        if (checkDate.before(dueDate)) {
            return 0;
        }
        
        long diffInMillies = checkDate.getTime() - dueDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public boolean isOverdue() {
        return returnDate == null && new Date().after(dueDate);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean isRenewed() {
        return renewed;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", member=" + member.getName() +
                ", book=" + book.getTitle() +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", overdue=" + isOverdue() +
                ", daysOverdue=" + getDaysOverdue() +
                ", fineAmount=" + fineAmount +
                '}';
    }
} 