package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import model.User;
import model.PremiumUser;
import model.RegularUser;
import dao.UserDAO;
import db.DBConnection; // Adaugă acest import!!

public class FoodDeliveryPlatform {
    public static void main(String[] args) {
        try {
            // Folosește clasa ta DBConnection!
            Connection conn = DBConnection.getConnection();
            UserDAO userDAO = new UserDAO(conn); // Pass connection

            List<User> users = new ArrayList<>();
            User user = new RegularUser("Ion Popescu", "Strada Principala 10", 100.0);
            userDAO.addUser(user); // Insert into DB
            users.add(user);
            users.add(new PremiumUser("Ana Maria", "Strada Florilor 5", 200.0, 0.10));
            users.add(new RegularUser("George Vasilescu", "Bulevardul Unirii 15", 50.0));

            for (User u : users) {
                System.out.println(u.getName());
            }
            System.out.println();

            users.sort(Comparator.comparingDouble(User::getBalance));

            for (User u : users) {
                System.out.println(u.getName() + "  " + u.getBalance());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}