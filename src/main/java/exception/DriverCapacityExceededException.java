package exception;

public class DriverCapacityExceededException extends RuntimeException {
    public DriverCapacityExceededException(String driverName) {
        super("Livratorul " + driverName + " a atins capacitatea maximă de comenzi.");
    }
}