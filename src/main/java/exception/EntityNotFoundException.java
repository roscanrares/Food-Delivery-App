package exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityType, int id) {
        super(entityType + " cu id-ul " + id + " nu a fost găsit.");
    }
}