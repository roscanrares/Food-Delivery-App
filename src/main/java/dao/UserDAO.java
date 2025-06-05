package dao;

import model.*;
import exception.InsufficientFundsException;
import service.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, address, balance, user_type, discount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getAddress());
            stmt.setDouble(3, user.getBalance());
            // Detectăm tipul și discountul
            if (user instanceof PremiumUser) {
                stmt.setString(4, "premium");
                stmt.setDouble(5, ((PremiumUser) user).getDiscount());
            } else {
                stmt.setString(4, "regular");
                stmt.setNull(5, Types.DOUBLE);
            }
            stmt.executeUpdate();
            // AUDIT
            AuditService.getInstance().logDAOAction(
                    "UserDAO", "addUser", "name=" + user.getName() + ",type=" + (user instanceof PremiumUser ? "premium" : "regular")
            );
        }
    }

    // READ (toți userii)
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        }
        // AUDIT
        AuditService.getInstance().logDAOAction(
                "UserDAO", "getAllUsers", "all"
        );
        return users;
    }

    // READ (după id)
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // AUDIT
                    AuditService.getInstance().logDAOAction(
                            "UserDAO", "getUserById", "id=" + id
                    );
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateUser(User user, int id) throws SQLException {
        String sql = "UPDATE users SET name=?, address=?, balance=?, user_type=?, discount=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getAddress());
            stmt.setDouble(3, user.getBalance());
            if (user instanceof PremiumUser) {
                stmt.setString(4, "premium");
                stmt.setDouble(5, ((PremiumUser) user).getDiscount());
            } else {
                stmt.setString(4, "regular");
                stmt.setNull(5, Types.DOUBLE);
            }
            stmt.setInt(6, id);
            stmt.executeUpdate();
            // AUDIT
            AuditService.getInstance().logDAOAction(
                    "UserDAO", "updateUser", "id=" + id + ",name=" + user.getName()
            );
        }
    }

    // DELETE
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            // AUDIT
            AuditService.getInstance().logDAOAction(
                    "UserDAO", "deleteUser", "id=" + id
            );
        }
    }

    // Helper pentru mapare
    private User mapRowToUser(ResultSet rs) throws SQLException {
        String userType = rs.getString("user_type");
        if ("premium".equals(userType)) {
            return new PremiumUser(
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("balance"),
                    rs.getDouble("discount")
            );
        } else {
            return new RegularUser(
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("balance")
            );
        }
    }
}