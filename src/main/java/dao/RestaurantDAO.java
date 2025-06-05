package dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {
    private Connection conn;

    public RestaurantDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void addRestaurant(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO restaurants (name, type, chef_specialty) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getType());

            // Exemplu: doar TraditionalRestaurant are chefSpecialty
            if (restaurant instanceof TraditionalRestaurant) {
                stmt.setString(3, ((TraditionalRestaurant) restaurant).getChefSpecialty());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            stmt.executeUpdate();
        }
        // Opțional: poți salva și meniul în altă masă (ex: restaurant_menu)
    }

    // READ (toate restaurantele)
    public List<Restaurant> getAllRestaurants() throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT * FROM restaurants";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                restaurants.add(mapRowToRestaurant(rs));
            }
        }
        return restaurants;
    }

    // READ (după id)
    public Restaurant getRestaurantById(int id) throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToRestaurant(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateRestaurant(Restaurant restaurant, int id) throws SQLException {
        String sql = "UPDATE restaurants SET name=?, type=?, chef_specialty=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getType());
            if (restaurant instanceof TraditionalRestaurant) {
                stmt.setString(3, ((TraditionalRestaurant) restaurant).getChefSpecialty());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deleteRestaurant(int id) throws SQLException {
        String sql = "DELETE FROM restaurants WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Helper pentru mapare la instanță corectă
    private Restaurant mapRowToRestaurant(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String name = rs.getString("name");
        // Adaptează la structura ta!
        switch (type) {
            case "Fast-Food":
                return new FastFoodRestaurant(name);
            case "Bakery":
                return new Bakery(name);
            case "Traditional Clasic":
                return new TraditionalRestaurant(name, rs.getString("chef_specialty"));
            default:
                // fallback generic (poate vrei să arunci excepție)
                throw new IllegalArgumentException("Unknown restaurant type: " + type);
        }
    }

    // (Opțional) Metode pentru gestionarea meniului separat, dacă ai un tabel restaurant_menu
}