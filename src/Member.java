import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Date joinDate;
    private Date expiryDate;
    private boolean active;
    private String memberType; // Student, Faculty, Regular, etc.
    private List<BorrowRecord> borrowHistory;
    private int maxBooksAllowed;
    private double fineAmount;

    public Member(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = new Date();
        this.expiryDate = calculateExpiryDate();
        this.active = true;
        this.memberType = "Regular";
        this.borrowHistory = new ArrayList<>();
        this.maxBooksAllowed = 3;
        this.fineAmount = 0.0;
    }

    public Member(int id, String name, String email, String phone, String address, 
                  String memberType, int maxBooksAllowed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = new Date();
        this.expiryDate = calculateExpiryDate();
        this.active = true;
        this.memberType = memberType;
        this.borrowHistory = new ArrayList<>();
        this.maxBooksAllowed = maxBooksAllowed;
        this.fineAmount = 0.0;
    }

    private Date calculateExpiryDate() {
        // Set expiry to 1 year from join date
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(joinDate);
        calendar.add(java.util.Calendar.YEAR, 1);
        return calendar.getTime();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void renewMembership() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(java.util.Calendar.YEAR, 1);
        this.expiryDate = calendar.getTime();
        this.active = true;
    }

    public boolean isActive() {
        return active && new Date().before(expiryDate);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public List<BorrowRecord> getBorrowHistory() {
        return borrowHistory;
    }

    public int getCurrentBorrowCount() {
        int count = 0;
        for (BorrowRecord record : borrowHistory) {
            if (record.getReturnDate() == null) {
                count++;
            }
        }
        return count;
    }

    public boolean canBorrow() {
        return isActive() && getCurrentBorrowCount() < maxBooksAllowed && fineAmount == 0;
    }

    public BorrowRecord borrowBook(Book book) {
        if (!canBorrow()) {
            return null;
        }
        
        if (!book.isAvailable() || !book.borrowBook()) {
            return null;
        }
        
        BorrowRecord record = new BorrowRecord(this, book);
        borrowHistory.add(record);
        return record;
    }

    public boolean returnBook(BorrowRecord record) {
        if (record == null || record.getReturnDate() != null) {
            return false;
        }
        
        record.returnBook();
        Book book = record.getBook();
        book.returnBook();
        
        // Calculate fine if any
        long daysOverdue = record.getDaysOverdue();
        if (daysOverdue > 0) {
            double fineRate = 1.0; // $1 per day
            fineAmount += daysOverdue * fineRate;
        }
        
        return true;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean payFine(double amount) {
        if (amount <= 0 || amount > fineAmount) {
            return false;
        }
        fineAmount -= amount;
        return true;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }
    
    // Validation
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
               email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") &&
               phone != null && phone.matches("^\\d{10}$");
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", memberType='" + memberType + '\'' +
                ", active=" + isActive() +
                ", currentBorrows=" + getCurrentBorrowCount() +
                ", fineAmount=" + fineAmount +
                '}';
    }
} 