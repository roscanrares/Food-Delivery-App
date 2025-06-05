package exception;

public class OrderAlreadyPaidException extends RuntimeException {
    public OrderAlreadyPaidException() {
        super("Comanda a fost deja plătită.");
    }
}