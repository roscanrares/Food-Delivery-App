package exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(double required, double available) {
        super("Fonduri insuficiente: ai " + available + " lei, dar este nevoie de " + required + " lei.");
    }
}