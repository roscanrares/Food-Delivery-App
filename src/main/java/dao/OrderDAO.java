package dao;

import model.*;
import java.sql.*;
import java.util.*;

public class OrderDAO {
    private final Connection conn;
    private final UserDAO userDAO;
    private final RestaurantDAO restaurantDAO;
    private final DriverDAO driverDAO;

    public OrderDAO(Connection conn, UserDAO userDAO, RestaurantDAO restaurantDAO, DriverDAO driverDAO) {
        this.conn = conn;
        this.userDAO = userDAO;
        this.restaurantDAO = restaurantDAO;
        this.driverDAO = driverDAO;
    }

    // CREATE
    public void addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, restaurant_id, driver_id, total_price, is_paid) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, order.getUser().getId());
            stmt.setInt(2, order.getRestaurant().getId());
            stmt.setInt(3, order.getDriver().getId());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.setBoolean(5, order.isPaid());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    addOrderItems(orderId, order.getItems());
                }
            }
        }
    }

    private void addOrderItems(int orderId, List<String> items) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, item_name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (String item : items) {
                stmt.setInt(1, orderId);
                stmt.setString(2, item);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    // READ ALL
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        }
        return orders;
    }

    // READ BY ID
    public Order getOrderById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToOrder(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateOrder(Order order, int id) throws SQLException {
        String sql = "UPDATE orders SET user_id=?, restaurant_id=?, driver_id=?, total_price=?, is_paid=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, order.getUser().getId());
            stmt.setInt(2, order.getRestaurant().getId());
            stmt.setInt(3, order.getDriver().getId());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.setBoolean(5, order.isPaid());
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
        // Poți actualiza și order_items aici dacă se modifică
    }

    // DELETE
    public void deleteOrder(int id) throws SQLException {
        // Mai întâi ștergi itemele
        String sql1 = "DELETE FROM order_items WHERE order_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        // Apoi comanda
        String sql2 = "DELETE FROM orders WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Helper pentru mapare din ResultSet la Order
    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        int orderId = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int restaurantId = rs.getInt("restaurant_id");
        int driverId = rs.getInt("driver_id");
        boolean isPaid = rs.getBoolean("is_paid");

        User user = userDAO.getUserById(userId);
        Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);
        DeliveryDriver driver = driverDAO.getDriverById(driverId);
        List<String> items = getOrderItems(orderId);

        Order order = new Order(user, restaurant, items, driver);
        if (isPaid && !order.isPaid()) {
            order.processPayment();
        }
        return order;
    }

    private List<String> getOrderItems(int orderId) throws SQLException {
        List<String> items = new ArrayList<>();
        String sql = "SELECT item_name FROM order_items WHERE order_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(rs.getString("item_name"));
                }
            }
        }
        return items;
    }
}