package dao;

import model.*;
import service.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    private Connection conn;

    public DriverDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE
    public void addDriver(DeliveryDriver driver) throws SQLException {
        String sql = "INSERT INTO drivers (name, vehicle_type, max_capacity, current_load, car_model, license_plate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getVehicleType());
            stmt.setInt(3, driver.getMaxCapacity());
            stmt.setInt(4, driver.getCurrentLoad());

            // Specific fields for subclasses
            if (driver instanceof CarDriver) {
                stmt.setString(5, ((CarDriver) driver).getCarModel());
                stmt.setNull(6, Types.VARCHAR);
            } else if (driver instanceof ScooterDriver) {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setString(6, ((ScooterDriver) driver).getLicensePlate());
            } else {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.executeUpdate();
            // AUDIT
            AuditService.getInstance().logDAOAction(
                    "DriverDAO", "addDriver", "name=" + driver.getName() + ",vehicleType=" + driver.getVehicleType()
            );
        }
    }

    // READ (all)
    public List<DeliveryDriver> getAllDrivers() throws SQLException {
        List<DeliveryDriver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM drivers";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                drivers.add(mapRowToDriver(rs));
            }
        }
        // AUDIT
        AuditService.getInstance().logDAOAction(
                "DriverDAO", "getAllDrivers", "all"
        );
        return drivers;
    }

    // READ (by id)
    public DeliveryDriver getDriverById(int id) throws SQLException {
        String sql = "SELECT * FROM drivers WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // AUDIT doar dacă există driver găsit
                    AuditService.getInstance().logDAOAction(
                            "DriverDAO", "getDriverById", "id=" + id
                    );
                    return mapRowToDriver(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateDriver(DeliveryDriver driver, int id) throws SQLException {
        String sql = "UPDATE drivers SET name=?, vehicle_type=?, max_capacity=?, current_load=?, car_model=?, license_plate=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getVehicleType());
            stmt.setInt(3, driver.getMaxCapacity());
            stmt.setInt(4, driver.getCurrentLoad());

            if (driver instanceof CarDriver) {
                stmt.setString(5, ((CarDriver) driver).getCarModel());
                stmt.setNull(6, Types.VARCHAR);
            } else if (driver instanceof ScooterDriver) {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setString(6, ((ScooterDriver) driver).getLicensePlate());
            } else {
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.setInt(7, id);
            stmt.executeUpdate();
            // AUDIT
            AuditService.getInstance().logDAOAction(
                    "DriverDAO", "updateDriver", "id=" + id + ",name=" + driver.getName()
            );
        }
    }

    // DELETE
    public void deleteDriver(int id) throws SQLException {
        String sql = "DELETE FROM drivers WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            // AUDIT
            AuditService.getInstance().logDAOAction(
                    "DriverDAO", "deleteDriver", "id=" + id
            );
        }
    }

    // Helper: mapare din ResultSet la DeliveryDriver corect
    private DeliveryDriver mapRowToDriver(ResultSet rs) throws SQLException {
        String vehicleType = rs.getString("vehicle_type");
        String name = rs.getString("name");
        int maxCapacity = rs.getInt("max_capacity");
        int currentLoad = rs.getInt("current_load");

        switch (vehicleType) {
            case "Masina":
                CarDriver car = new CarDriver(name, rs.getString("car_model"));
                for (int i = 0; i < currentLoad; i++) car.acceptOrder();
                return car;
            case "Scuter":
                ScooterDriver scooter = new ScooterDriver(name, rs.getString("license_plate"));
                for (int i = 0; i < currentLoad; i++) scooter.acceptOrder();
                return scooter;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }
}