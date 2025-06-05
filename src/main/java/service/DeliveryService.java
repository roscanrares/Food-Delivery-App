package service;

import exception.*;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliveryService {
    private final List<User> users = new ArrayList<>();
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final List<DeliveryDriver> drivers = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    // --- User Methods ---

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserByName(String userName) {
        return users.stream()
                .filter(u -> u.getName().equals(userName))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User", userName));
    }

    // --- Restaurant Methods ---

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        return restaurants.stream()
                .filter(r -> r.getName().equals(restaurantName))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Restaurant", restaurantName));
    }

    // --- Driver Methods ---

    public void addDriver(DeliveryDriver driver) {
        drivers.add(driver);
    }

    public DeliveryDriver assignDriver() {
        return drivers.stream()
                .filter(DeliveryDriver::isAvailable)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("DeliveryDriver", "No available driver"));
    }

    // --- Order Methods ---

    public void placeOrder(String userName, String restaurantName, List<String> items) {
        User user = getUserByName(userName);
        Restaurant restaurant = getRestaurantByName(restaurantName);
        DeliveryDriver driver = assignDriver();

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Lista de produse nu poate fi goală.");
        }

        try {
            Order order = new Order(user, restaurant, items, driver);
            order.processPayment();
            driver.acceptOrder();
            orders.add(order);
            // Mesajele de succes pot fi returnate sau logate, nu printate direct!
        } catch (MenuItemNotFoundException | InsufficientBalanceException |
                 DriverCapacityExceededException | OrderAlreadyPaidException e) {
            // Poți loga, returna mesajul către UI, sau arunca mai departe
            throw e;
        }
    }

    public void completeDelivery(Order order) {
        order.getDriver().completeOrder();
        // Poți loga sau returna mesajul de livrare către UI
    }

    public List<Order> getOrdersForUser(String userName) {
        return orders.stream()
                .filter(o -> o.getUser().getName().equals(userName))
                .toList();
    }

    public List<Order> getActiveOrdersForDriver(String driverName) {
        return orders.stream()
                .filter(o -> o.getDriver().getName().equals(driverName) && !o.isPaid())
                .toList();
    }

    // --- Menu Display ---

    public void displayMenu(String restaurantName) {
        Restaurant restaurant = getRestaurantByName(restaurantName);
        System.out.println("\nMeniu " + restaurant.getType() + " " + restaurant.getName());
        System.out.println("----------------------------------");
        restaurant.getMenu().forEach((item, price) ->
                System.out.printf("- %s: %.2f lei%n", item, price));
    }

    // --- List Management (for tests or UI) ---

    public List<Restaurant> getRestaurants() {
        return new ArrayList<>(restaurants);
    }

    public List<DeliveryDriver> getDrivers() {
        return new ArrayList<>(drivers);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    // --- If scaling to DB, replace lists with DAO fields and forward calls! ---
}