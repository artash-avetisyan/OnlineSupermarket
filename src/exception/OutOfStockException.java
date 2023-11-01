package exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
        super("Out of stock!");
    }

    public OutOfStockException(String message) {
        super(message);
    }
}
