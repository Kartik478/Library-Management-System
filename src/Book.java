public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private boolean available;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private String location;
    private java.util.Date dateAdded;

    public Book(int id, String title, String author, String publisher, int year, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.available = available;
        this.totalCopies = 1;
        this.availableCopies = available ? 1 : 0;
        this.dateAdded = new java.util.Date();
    }
    
    public Book(int id, String title, String author, String publisher, int year, 
                boolean available, String isbn, String category, int totalCopies, 
                String location) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.available = available;
        this.isbn = isbn;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = available ? totalCopies : 0;
        this.location = location;
        this.dateAdded = new java.util.Date();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailable() {
        return available;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public String getCategory() {
        return category;
    }
    
    public int getTotalCopies() {
        return totalCopies;
    }
    
    public int getAvailableCopies() {
        return availableCopies;
    }
    
    public String getLocation() {
        return location;
    }
    
    public java.util.Date getDateAdded() {
        return dateAdded;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    // Borrowing functionality
    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            if (availableCopies == 0) {
                available = false;
            }
            return true;
        }
        return false;
    }
    
    public boolean returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            available = true;
            return true;
        }
        return false;
    }
    
    public boolean addCopy() {
        totalCopies++;
        availableCopies++;
        available = true;
        return true;
    }
    
    public boolean removeCopy() {
        if (totalCopies > 1 && availableCopies > 0) {
            totalCopies--;
            availableCopies--;
            if (availableCopies == 0) {
                available = false;
            }
            return true;
        }
        return false;
    }
    
    // Validation
    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
               author != null && !author.trim().isEmpty() &&
               year > 0 && year <= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", available=" + available +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                '}';
    }
}
