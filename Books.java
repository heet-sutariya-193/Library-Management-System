package ioom_assi6;

public class Books {
    private final String title;
    private final String author;
    private final  int bookId;
    private int copies;
    private boolean available;

    public Books(String title, String author, int bookId, int copies) {
        this.title = title;
        this.author = author;
        this.bookId = bookId;
        this.copies = copies;
        this.available = copies > 0;
    }

    public void borrowBook() throws LibraryException {
        if (copies <= 0) {
            available = false;
            throw new LibraryException("No copies available");
        }
        copies--;
        available = copies > 0;
    }

    public void returnBook() {
        copies++;
        available = true;
    }

    public boolean matchesSearch(String query) {
        return String.valueOf(bookId).equals(query) || 
               title.toLowerCase().contains(query.toLowerCase()) ||
               author.toLowerCase().contains(query.toLowerCase());
    }

    // Getters and setters
    public boolean isAvailable() { return available; }
    public int getCopies() { return copies; }
    public void setCopies(int copies) { 
        this.copies = copies;
        this.available = copies > 0;
    }
    public String getTitle() { return title; }
    public int getBookId() { return bookId; }
    public String getAuthor() { return author; }
}