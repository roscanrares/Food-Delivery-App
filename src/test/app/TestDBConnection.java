package app;

import db.DBConnection;
import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Conexiunea la baza de date a reușit!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}