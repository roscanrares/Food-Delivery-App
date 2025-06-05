package service;

import dao.UserDAO;
import dao.RestaurantDAO;
import dao.DriverDAO;
import dao.OrderDAO;
import model.User;
import model.Restaurant;
import model.DeliveryDriver;
import model.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PlatformManager {
    private static PlatformManager instance;

    private final UserDAO userDAO;
    private final RestaurantDAO restaurantDAO;
    private final DriverDAO driverDAO;
    private final OrderDAO orderDAO;

    // Singleton pattern - Constructor privat
    private PlatformManager(Connection connection) {
        this.userDAO = new UserDAO(connection);
        this.restaurantDAO = new RestaurantDAO(connection);
        this.driverDAO = new DriverDAO(connection);
        this.orderDAO = new OrderDAO(connection, userDAO, restaurantDAO, driverDAO); // <-- FIX
    }

    // Singleton access - permite inițializarea doar cu primul Connection dat
    public static PlatformManager getInstance(Connection connection) {
        if (instance == null) {
            instance = new PlatformManager(connection);
        }
        return instance;
    }

    // --- USER CRUD ---
    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public User getUserById(int id) throws SQLException {
        return userDAO.getUserById(id);
    }

    public void updateUser(User user, int id) throws SQLException {
        userDAO.updateUser(user, id);
    }

    public void deleteUser(int id) throws SQLException {
        userDAO.deleteUser(id);
    }

    // --- RESTAURANT CRUD ---
    public void addRestaurant(Restaurant restaurant) throws SQLException {
        restaurantDAO.addRestaurant(restaurant);
    }

    public List<Restaurant> getAllRestaurants() throws SQLException {
        return restaurantDAO.getAllRestaurants();
    }

    public Restaurant getRestaurantById(int id) throws SQLException {
        return restaurantDAO.getRestaurantById(id);
    }

    public void updateRestaurant(Restaurant restaurant, int id) throws SQLException {
        restaurantDAO.updateRestaurant(restaurant, id);
    }

    public void deleteRestaurant(int id) throws SQLException {
        restaurantDAO.deleteRestaurant(id);
    }

    // --- DRIVER CRUD ---
    public void addDriver(DeliveryDriver driver) throws SQLException {
        driverDAO.addDriver(driver);
    }

    public List<DeliveryDriver> getAllDrivers() throws SQLException {
        return driverDAO.getAllDrivers();
    }

    public DeliveryDriver getDriverById(int id) throws SQLException {
        return driverDAO.getDriverById(id);
    }

    public void updateDriver(DeliveryDriver driver, int id) throws SQLException {
        driverDAO.updateDriver(driver, id);
    }

    public void deleteDriver(int id) throws SQLException {
        driverDAO.deleteDriver(id);
    }

    // --- ORDER CRUD ---
    public void addOrder(Order order) throws SQLException {
        orderDAO.addOrder(order);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public Order getOrderById(int id) throws SQLException {
        return orderDAO.getOrderById(id);
    }

    public void updateOrder(Order order, int id) throws SQLException {
        orderDAO.updateOrder(order, id);
    }

    public void deleteOrder(int id) throws SQLException {
        orderDAO.deleteOrder(id);
    }
}