# Library Management System

A comprehensive Java-based Library Management System that handles book borrowing, returns, fines management, and user administration for both students and librarians.

## 📚 Features

### Student Features
- **User Authentication** - Secure login with student ID and password
- **Book Borrowing** - Borrow available books with a limit of 7 books per student
- **Book Returns** - Return borrowed books with automatic fine calculation
- **Fine Management** - View and pay overdue fines (₹10 per day)
- **Book Search** - Search books by ID, title, or author
- **Password Management** - Change password functionality
- **Borrowed Books View** - See currently borrowed books with due dates

### Librarian Features
- **Registration & Login** - Librarian account management
- **Book Management** - Add, remove, and update book copies
- **Student Registration** - Register new students in the system
- **Inventory Management** - View and manage assigned books
- **Search Functionality** - Comprehensive book search

## 🏗️ System Architecture

### Core Classes
- **Main** - Application entry point and menu navigation
- **User** - Abstract base class for all users
- **Student** - Handles student operations and book transactions
- **Librarian** - Manages library inventory and student registrations
- **Books** - Represents book entities with availability tracking
- **LibraryException** - Custom exception handling for library operations

### Data Persistence
- **CSV Files** - All data stored in CSV format:
  - `books.csv` - Book catalog with copies information
  - `students.csv` - Student records with borrowed books and fines
  - `librarians.csv` - Librarian accounts with managed books

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher
- Basic terminal/command line knowledge

### Installation & Execution

1. **Compile the Project:**
   ```bash
   javac -d .. *.java

Run the Application:
java -cp .. ioom_assi6.Main
