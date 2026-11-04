package exceptions;

public class DatabaseException extends Exception {
    // Thrown when something goes wrong in the database layer
    public DatabaseException(String message) {
        super(message);
    }
}
