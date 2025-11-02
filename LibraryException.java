package ioom_assi6;

public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }

    public static class InvalidChoice extends LibraryException {
        public InvalidChoice() { super("Invalid choice!"); }
    }
    public static class InvalidCredentials extends LibraryException {
        public InvalidCredentials() { super("Invalid credentials!"); }
    }
    public static class PaymentError extends LibraryException {
        public PaymentError(String msg) { super(msg); }
    }
    public static class BookNotFound extends LibraryException {
        public BookNotFound() { super("Book not found in the library!"); }
    }
}
