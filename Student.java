package ioom_assi6;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class Student extends User {
    private final String studentId;
    private final String department;
    private final HashSet<Books> borrowedBooks;
    private double fine;
    private final HashSet<BookTransaction> transactions;

    public Student(String name, String email, String password, String studentId, String department) {
        super(name, email, password);
        this.studentId = studentId;
        this.department = department;
        this.borrowedBooks = new HashSet<>();
        this.transactions = new HashSet<>();
        this.fine = 0.0;
    }
   
    // Added getter for department
    public String getDepartment() { return department; }

    public void borrowBook(Books book, LocalDate borrowDate) throws LibraryException {
        if (borrowedBooks.size() >= 7) {
            throw new LibraryException("Maximum borrowing limit reached (7 books)");
        }
        if (!book.isAvailable()) {
            throw new LibraryException("Book not available");
        }
        book.borrowBook();
        borrowedBooks.add(book);
        transactions.add(new BookTransaction(book, borrowDate));
    }

    public double returnBook(Books book, LocalDate returnDate) throws LibraryException {
        if (!borrowedBooks.contains(book)) {
            throw new LibraryException("Book not borrowed by this student");
        }
        
        BookTransaction transaction = transactions.stream()
                .filter(t -> t.getBook().equals(book))
                .findFirst()
                .orElseThrow(() -> new LibraryException("Transaction not found"));
        
        long daysOverdue = ChronoUnit.DAYS.between(transaction.getDueDate(), returnDate);
        if (daysOverdue > 0) {
            double calculatedFine = daysOverdue * 10.0;
            fine += calculatedFine;
        }
        
        book.returnBook();
        borrowedBooks.remove(book);
        transactions.remove(transaction);
        return fine;
    }
    public String getBorrowedBooksInfo() {
        if (transactions.isEmpty()) {
            return "No books currently borrowed";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-30s %-15s %-15s%n", 
                "Book ID", "Title", "Borrow Date", "Due Date"));
        
        transactions.forEach(t -> {
            Books book = t.getBook();
            sb.append(String.format("%-10d %-30s %-15s %-15s%n",
                    book.getBookId(),
                    book.getTitle().length() > 28 ? 
                        book.getTitle().substring(0, 25) + "..." : book.getTitle(),
                    t.getBorrowDate(),
                    t.getDueDate()));
        });
        return sb.toString();
    }
    

    public LocalDate getTransactionDate(int bookId) {
        return transactions.stream()
                .filter(t -> t.getBook().getBookId() == bookId)
                .findFirst()
                .map(BookTransaction::getBorrowDate)
                .orElse(null);
    }

    // Getters and other methods
    public String getStudentId() { return studentId; }
    public double getFine() { return fine; }
    public void setFine(double fine) {this.fine=fine; }
    public HashSet<Books> getBorrowedBooks()
    {
        return borrowedBooks;
    }
    public void payFine(double amount) { 
        if(amount > fine) throw new IllegalArgumentException("Cannot pay more than current fine");
        fine -= amount; 
    }
    
    @Override
    public String change_password(String newPassword) {
        setPassword(newPassword);
        return "Password changed successfully!";
    }

    private class BookTransaction {
        private final Books book;
        private final LocalDate borrowDate;
        private final LocalDate dueDate;

        public BookTransaction(Books book, LocalDate borrowDate) {
            this.book = book;
            this.borrowDate = borrowDate;
            this.dueDate = borrowDate.plusDays(14);
        }

        public LocalDate getDueDate() { return dueDate; }
        public Books getBook() { return book; }
        public LocalDate getBorrowDate(){return borrowDate;}
    }

    
}