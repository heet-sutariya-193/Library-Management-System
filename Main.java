package ioom_assi6;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
//javac -d .. *.java
//java -cp .. ioom_assi6.Main

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Student> students = new ArrayList<>();
    private static List<Librarian> librarians = new ArrayList<>();
    private static List<Books> books = new ArrayList<>();

    public static void main(String[] args) {
        loadInitialData();
        
        while (true) {
            System.out.println("\nWelcome to our Library System,please select one of the options below");
            System.out.println("1. Student Login");
            System.out.println("2. Librarian Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            
            int choice = readInt();
            
            switch (choice) {
                case 1 -> studentMenu();
                case 2 -> librarianMenu();
                case 3 -> {
                    saveData();
                    System.exit(0);
                }
                default -> {
                    try {
                        throw new LibraryException.InvalidChoice();
                    } catch (LibraryException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                
            }
        }
    }

    private static void studentMenu() {
        System.out.println("\n1. Login\n2. Back");
        int choice = readInt();
        if (choice == 1)  loginStudent();
    }


    private static void loginStudent() {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter password: ");
        String pw = scanner.nextLine();

        Student student = students.stream()
                .filter(s -> s.getStudentId().equals(id) && s.get_password().equals(pw))
                .findFirst()
                .orElse(null);

        if (student != null) studentOperations(student);
        else {
            try {
                throw new LibraryException.InvalidCredentials();
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void studentOperations(Student student) {
        while (true) {
            System.out.println("\nwelcom to Student Portal,please select one of the below options");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Fines");
            System.out.println("4. Pay Fines");
            System.out.println("5. Change Password");
            System.out.println("6. View Borrowed Books");
            System.out.println("7. Search Books");
            System.out.println("0. Logout");
            
            switch (readInt()) {
                case 1 -> borrowBook(student);
                case 2 -> returnBook(student);
                case 3 -> System.out.println("Current fines: ₹" + student.getFine());
                case 4 -> payFine(student);
                case 5 -> changePassword(student);
                case 6 -> { // Handle view borrowed books
                    System.out.println("\n=== Your Borrowed Books ===");
                    System.out.println(student.getBorrowedBooksInfo());
                }
                case 7 -> searchBooks();
                case 0 -> { return; }
                default -> {
                    try {
                        throw new LibraryException.InvalidChoice();
                    } catch (LibraryException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                
            }
        }
    }
    private static void registerStudentByLibrarian(Librarian librarian) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter temporary password: ");
        String password = scanner.nextLine();
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter department: ");
        String dept = scanner.nextLine();
    
        Student student = new Student(name, email, password, studentId, dept);
        students.add(student);
        System.out.println("Student registered successfully by " + librarian.getname());
    }

    private static void borrowBook(Student student) {
        System.out.println("Available Books:");
        books.stream()
            .filter(Books::isAvailable)
            .forEach(b -> System.out.println(b.getBookId() + ": " + b.getTitle()));
        
        System.out.print("Enter book ID: ");
        int bookId = readInt();
        Books book = findBook(bookId);
        
        if (book != null) {
            try {
                student.borrowBook(book, LocalDate.now());
                System.out.println("Book borrowed successfully!");
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Book not found!");
        }
    }

    private static void returnBook(Student student) {
        System.out.print("Enter book ID to return: ");
        int bookId = readInt();
        Books book = findBook(bookId);
        
        if (book == null) {
            System.out.println("Invalid book ID!");
            return;
        }

        System.out.print("Enter return date (yyyy-mm-dd): ");
        try {
            LocalDate returnDate = LocalDate.parse(scanner.nextLine());
            double fine = student.returnBook(book, returnDate);
            System.out.println("Book returned successfully!");
            if (fine > 0) {
                System.out.println("Added fine: ₹" + fine);
            }
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void payFine(Student student) {
        System.out.println("Current fine: ₹" + student.getFine());
        System.out.print("Enter amount to pay: ");
        double amount = readDouble();
        
        try {
            student.payFine(amount);
            System.out.println("Payment successful!");
        } catch (IllegalArgumentException e) {
            try {
                throw new LibraryException.PaymentError(e.getMessage());
            } catch (LibraryException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        
    }

    private static void changePassword(User user) {
        System.out.print("Enter new password: ");
        String newPw = scanner.nextLine();
        System.out.println(user.change_password(newPw));
    }

    private static void librarianMenu() {
        System.out.println("\n1. Register\n2. Login\n3. Back");
        int choice = readInt();
        if (choice == 1) registerLibrarian();
        else if (choice == 2) loginLibrarian();
    }

    private static void loginLibrarian() {
        System.out.print("Enter employee ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter password: ");
        String pw = scanner.nextLine();

        Librarian librarian = librarians.stream()
                .filter(l -> l.getEmployeeId().equals(id) && l.get_password().equals(pw))
                .findFirst()
                .orElse(null);

        if (librarian != null) librarianOperations(librarian);
        else System.out.println("Invalid credentials!");
    }

    private static void registerLibrarian() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter employee ID: ");
        String empId = scanner.nextLine();
    
        Librarian librarian = new Librarian(name, email, password, empId);
        librarians.add(librarian);
        System.out.println("Librarian registration successful!");
    }

    private static void librarianOperations(Librarian librarian) {
        while (true) {
            System.out.println("\n==== Librarian Portal ====");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("2. register student");
            System.out.println("3. Update Book Copies");
            System.out.println("4. List All Books");
            System.out.println("5. View Managed Books");
            System.out.println("6. Search Books");
            System.out.println("7. Change Password");
            System.out.println("0. Logout");
            
            switch (readInt()) {
                case 1 -> addBook(librarian);
                case 2 -> removeBook(librarian);
                case 3 -> registerStudentByLibrarian(librarian);
                case 4 -> updateCopies(librarian);
                case 5 -> listBooks();
                case 6 -> {
                    System.out.println("\n=== Managed Books ===");
                    System.out.println(librarian.getManagedBooksInfo());
                }
                case 7 -> searchBooks();
                case 8 -> changePassword(librarian);
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void addBook(Librarian librarian) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book ID: ");
        int id = readInt();
        System.out.print("Enter initial copies: ");
        int copies = readInt();

        Books book = new Books(title, author, id, copies);
        books.add(book);
        librarian.addBook(book);
        System.out.println("Book added successfully!");
    }

    private static void removeBook(Librarian librarian) {
        System.out.print("Enter book ID to remove: ");
        int id = readInt();
        Books book = findBook(id);
        
        if (book != null) {
            try {
                librarian.removeBook(book);
                books.remove(book);
                System.out.println("Book removed successfully!");
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Book not found!");
        }
    }

    private static void updateCopies(Librarian librarian) {
        System.out.print("Enter book ID: ");
        int id = readInt();
        Books book = findBook(id);
        
        if (book != null) {
            System.out.print("Enter new copies count: ");
            int copies = readInt();
            librarian.updateBookCopies(book, copies);
            System.out.println("Copies updated successfully!");
        } else {
            System.out.println("Book not found!");
        }
    }

    private static void listBooks() {
        System.out.println("\n==== All Books ====");
        books.forEach(b -> System.out.println(
            b.getBookId() + " | " + b.getTitle() + 
            " | Available: " + b.isAvailable() + 
            " | Copies: " + b.getCopies()
        ));
    }

    private static Books findBook(int id) {
        return books.stream()
                .filter(b -> b.getBookId() == id)
                .findFirst()
                .orElse(null);
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    private static void searchBooks() {
        System.out.print("Enter Book ID or Title to search: ");
        String query = scanner.nextLine().trim();
        
        List<Books> results = books.stream()
            .filter(b -> String.valueOf(b.getBookId()).equals(query) || 
                        b.getTitle().toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());
    
        if(results.isEmpty()) {
            System.out.println("No books found matching your search");
        } else {
            System.out.println("\nSearch Results:");
            System.out.println("-----------------------------------------------------------");
            System.out.printf("%-10s %-30s %-25s %-10s %-6s%n", 
                "Book ID", "Title", "Author", "Available", "Copies");
            results.forEach(b -> {
                System.out.printf("%-10d %-30s %-25s %-10s %-6d%n",
                    b.getBookId(),
                    b.getTitle().length() > 28 ? b.getTitle().substring(0, 25) + "..." : b.getTitle(),
                    b.getAuthor().length() > 23 ? b.getAuthor().substring(0, 20) + "..." : b.getAuthor(),
                    b.isAvailable() ? "Yes" : "No",
                    b.getCopies());
            });
            System.out.println("-----------------------------------------------------------");
        }
    }

    private static void loadInitialData() {
    try {
        // Load books first since others depend on them
        if (Files.exists(Paths.get("books.csv"))) {
            books = Files.lines(Paths.get("books.csv"))
                    .skip(1) // Skip header
                    .map(line -> {
                        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        return new Books(
                            parts[1].replace("\"", ""), // title
                            parts[2].replace("\"", ""), // author
                            Integer.parseInt(parts[0]),  // book_id
                            Integer.parseInt(parts[3])); // copies
                    })
                    .collect(Collectors.toList());
        }

        // Load students with their borrowed books
        if (Files.exists(Paths.get("students.csv"))) {
            students = Files.lines(Paths.get("students.csv"))
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        Student student = new Student(
                            parts[0].replace("\"", ""), // name
                            parts[1].replace("\"", ""), // email
                            parts[2].replace("\"", ""), // password
                            parts[3].replace("\"", ""), // student_id
                            parts[4].replace("\"", ""));// department
                        
                        // Set fine if exists
                        if (parts.length > 5 && !parts[5].isEmpty()) {
                            student.setFine(Double.parseDouble(parts[5]));
                        }
                        
                        // Add borrowed books if exists
                        if (parts.length > 6 && !parts[6].equals("\"\"")) {
                            String[] bookEntries = parts[6].replace("\"", "").split(",");
                            for (String entry : bookEntries) {
                                String[] bookData = entry.split(":");
                                int bookId = Integer.parseInt(bookData[0]);
                                LocalDate borrowDate = LocalDate.parse(bookData[1]);
                                
                                books.stream()
                                    .filter(b -> b.getBookId() == bookId)
                                    .findFirst()
                                    .ifPresent(book -> {
                                        try {
                                            student.borrowBook(book, borrowDate);
                                        } catch (LibraryException e) {
                                            System.err.println("Error loading book for student: " + e.getMessage());
                                        }
                                    });
                            }
                        }
                        return student;
                    })
                    .collect(Collectors.toList());
        }

        
        
        // Load librarians with their managed books
        if (Files.exists(Paths.get("librarians.csv"))) {
            librarians = Files.lines(Paths.get("librarians.csv"))
                    .skip(1)
                    .map(line -> {
                        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                        Librarian librarian = new Librarian(
                            parts[0].replace("\"", ""), // name
                            parts[1].replace("\"", ""), // email
                            parts[2].replace("\"", ""), // password
                            parts[3].replace("\"", ""));// employee_id
                        
                        // Add managed books if exists
                        if (parts.length > 4 && !parts[4].equals("\"\"")) {
                            String[] bookIds = parts[4].replace("\"", "").split(",");
                            for (String bookId : bookIds) {
                                books.stream()
                                    .filter(b -> b.getBookId() == Integer.parseInt(bookId))
                                    .findFirst()
                                    .ifPresent(librarian::addBook);
                            }
                        }
                        return librarian;
                    })
                    .collect(Collectors.toList());
        }
    } catch (IOException e) {
        System.out.println("Error loading initial data: " + e.getMessage());
    }
}
private static void saveData() {
    try {
        // Save books
        Files.write(Paths.get("books.csv"), 
            ("book_id,title,author,copies\n" + 
             books.stream()
                .map(b -> String.format("%d,\"%s\",\"%s\",%d",
                    b.getBookId(), b.getTitle(), b.getAuthor(), b.getCopies()))
                .collect(Collectors.joining("\n")))
            .getBytes(StandardCharsets.UTF_8));

        // Save students
        Files.write(Paths.get("students.csv"), 
            ("name,email,password,student_id,department,fine,borrowed_books\n" + 
             students.stream()
                .map(s -> {
                    String borrowedBooks = s.getBorrowedBooks().stream()
                        .map(b -> b.getBookId() + ":" + s.getTransactionDate(b.getBookId()))
                        .collect(Collectors.joining(","));
                    return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\"",
                        s.getname(), s.get_email(), s.get_password(), 
                        s.getStudentId(), s.getDepartment(), s.getFine(),
                        borrowedBooks.isEmpty() ? "" : borrowedBooks);
                })
                .collect(Collectors.joining("\n")))
            .getBytes(StandardCharsets.UTF_8));

        // Save librarians
        Files.write(Paths.get("librarians.csv"), 
            ("name,email,password,employee_id,managed_books\n" + 
             librarians.stream()
                .map(l -> {
                    String managedBooks = l.getManagedBooks().stream()
                        .map(b -> String.valueOf(b.getBookId()))
                        .collect(Collectors.joining(","));
                    return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        l.getname(), l.get_email(), l.get_password(), 
                        l.getEmployeeId(), managedBooks.isEmpty() ? "" : managedBooks);
                })
                .collect(Collectors.joining("\n")))
            .getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
        System.out.println("Error saving data: " + e.getMessage());
    }
}
}
