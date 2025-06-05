package exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String item) {
        super("Produsul \"" + item + "\" nu există în meniu.");
    }
}