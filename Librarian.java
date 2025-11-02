package ioom_assi6;
import java.util.HashSet;

public class Librarian extends User {
    private final String employeeId;
    private final HashSet<Books> managedBooks;

    public Librarian(String name, String email, String password, String employeeId) {
        super(name, email, password);
        this.employeeId = employeeId;
        this.managedBooks = new HashSet<>();
    }

    public void addBook(Books book) {
        managedBooks.add(book);
    }

    public void removeBook(Books book) throws LibraryException {
        if (!managedBooks.contains(book)) {
            throw new LibraryException("Book not managed by this librarian");
        }
        managedBooks.remove(book);
    }

    public void updateBookCopies(Books book, int newCopies) {
        book.setCopies(newCopies);
    }
    public String getManagedBooksInfo() {
        if (managedBooks.isEmpty()) {
            return "No books currently being managed";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-30s %-25s %-6s%n", 
                "Book ID", "Title", "Author", "Copies"));
        
        managedBooks.forEach(book -> {
            sb.append(String.format("%-10d %-30s %-25s %-6d%n",
                    book.getBookId(),
                    book.getTitle().length() > 28 ? 
                        book.getTitle().substring(0, 25) + "..." : book.getTitle(),
                    book.getAuthor().length() > 23 ? 
                        book.getAuthor().substring(0, 20) + "..." : book.getAuthor(),
                    book.getCopies()));
        });
        return sb.toString();
    }
    @Override
    public String change_password(String newPassword) {
        setPassword(newPassword);
        return "Librarian password changed successfully!";
    }

    public String getEmployeeId() { return employeeId; }
    public HashSet<Books> getManagedBooks() { return managedBooks; }
}