import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/fooddelivery";
        String user = "postgres";
        String password = "parola_ta_aici"; // Înlocuiește cu parola ta

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Conexiune cu succes!");
            } else {
                System.out.println("Nu s-a putut realiza conexiunea.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}