public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private boolean available;

    public Book(int id, String title, String author, String publisher, int year, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.available = available;
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
}
