package exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityType, int id) {
        super(entityType + " cu id-ul " + id + " nu a fost găsit.");
    }

    // Adaugă acest constructor:
    public EntityNotFoundException(String entityType, String name) {
        super(entityType + " cu numele '" + name + "' nu a fost găsit.");
    }
}