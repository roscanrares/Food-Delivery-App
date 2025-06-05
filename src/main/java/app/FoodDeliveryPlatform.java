package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.User;
import model.PremiumUser;
import model.RegularUser;
import model.DeliveryDriver;
import model.Restaurant;
import model.FastFoodRestaurant;
import model.Bakery;
import model.TraditionalRestaurant;
import model.CarDriver;
import model.ScooterDriver;
import db.DBConnection;
import service.PlatformManager;

public class FoodDeliveryPlatform {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            PlatformManager manager = PlatformManager.getInstance(conn);

            // Adaugă useri
            User user1 = new RegularUser("Ion Popescu", "Strada Principala 10", 100.0);
            User user2 = new PremiumUser("Ana Maria", "Strada Florilor 5", 200.0, 0.10);
            User user3 = new RegularUser("George Vasilescu", "Bulevardul Unirii 15", 50.0);

            manager.addUser(user1);
            manager.addUser(user2);
            manager.addUser(user3);

            // Adaugă livratori (subclase concrete: CarDriver, ScooterDriver)
            DeliveryDriver driver1 = new CarDriver("Mihai Masina", "Dacia Logan");
            DeliveryDriver driver2 = new ScooterDriver("Andreea Scuter", "CJ12ABC");

            manager.addDriver(driver1);
            manager.addDriver(driver2);

            // Adaugă restaurante (subclase concrete: FastFoodRestaurant, Bakery, TraditionalRestaurant)
            Restaurant restaurant1 = new FastFoodRestaurant("Pizza House");
            Restaurant restaurant2 = new Bakery("Panemar");
            Restaurant restaurant3 = new TraditionalRestaurant("Casa Bunicii", "Ciorba de burta");

            manager.addRestaurant(restaurant1);
            manager.addRestaurant(restaurant2);
            manager.addRestaurant(restaurant3);

            // Afișează toți userii
            System.out.println("===== Useri inregistrati =====");
            List<User> users = manager.getAllUsers();
            for (User u : users) {
                System.out.println(u.getName() + " | " + u.getAddress() + " | " + u.getBalance());
            }

            // Afișează toți livratorii
            System.out.println("\n===== Livratori disponibili =====");
            for (DeliveryDriver d : manager.getAllDrivers()) {
                System.out.println(d.getName() + " | " + d.getVehicleType() + " | " + d.getCurrentLoad() + "/" + d.getMaxCapacity());
            }

            // Șterge un user și un restaurant
            if (!users.isEmpty()) {
                int idDeSters = users.get(0).getId(); // presupunem că ID-ul este setat după inserare
                manager.deleteUser(idDeSters);
                System.out.println("\nUserul cu id " + idDeSters + " a fost șters.");
            }

            List<Restaurant> restaurante = manager.getAllRestaurants();
            if (!restaurante.isEmpty()) {
                int idRestaurantDeSters = restaurante.get(0).getId();
                manager.deleteRestaurant(idRestaurantDeSters);
                System.out.println("Restaurantul cu id " + idRestaurantDeSters + " a fost șters.");
            }

            // Afișează userii și restaurantele după ștergere
            System.out.println("\n===== Useri după ștergere =====");
            for (User u : manager.getAllUsers()) {
                System.out.println(u.getName() + " | " + u.getAddress() + " | " + u.getBalance());
            }

            System.out.println("\n===== Restaurante după ștergere =====");
            for (Restaurant r : manager.getAllRestaurants()) {
                System.out.println(r.getName() + " | " + r.getType());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}