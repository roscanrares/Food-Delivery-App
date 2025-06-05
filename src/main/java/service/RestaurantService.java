package service;

import dao.RestaurantDAO;
import exception.EntityNotFoundException;
import model.Restaurant;

import java.sql.SQLException;
import java.util.List;

public class RestaurantService {
    private final RestaurantDAO restaurantDAO;

    public RestaurantService(RestaurantDAO restaurantDAO) {
        this.restaurantDAO = restaurantDAO;
    }

    public void addRestaurant(Restaurant restaurant) throws SQLException {
        restaurantDAO.addRestaurant(restaurant);
    }

    public List<Restaurant> getAllRestaurants() throws SQLException {
        return restaurantDAO.getAllRestaurants();
    }

    public Restaurant getRestaurantById(int id) throws SQLException {
        Restaurant restaurant = restaurantDAO.getRestaurantById(id);
        if (restaurant == null) throw new EntityNotFoundException("Restaurant", id);
        return restaurant;
    }

    public void updateRestaurant(Restaurant restaurant, int id) throws SQLException {
        getRestaurantById(id);
        restaurantDAO.updateRestaurant(restaurant, id);
    }

    public void deleteRestaurant(int id) throws SQLException {
        getRestaurantById(id);
        restaurantDAO.deleteRestaurant(id);
    }

    // Metode pentru accesarea și modificarea meniului direct din Restaurant
    public void addMenuItem(int restaurantId, String itemName, double price) throws SQLException {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.getMenu().put(itemName, price);
        restaurantDAO.updateRestaurant(restaurant, restaurantId);
    }

    public void removeMenuItem(int restaurantId, String itemName) throws SQLException {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.getMenu().remove(itemName);
        restaurantDAO.updateRestaurant(restaurant, restaurantId);
    }

    public void updateMenuItemPrice(int restaurantId, String itemName, double newPrice) throws SQLException {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (!restaurant.getMenu().containsKey(itemName)) {
            throw new EntityNotFoundException("MenuItem", itemName);
        }
        restaurant.getMenu().put(itemName, newPrice);
        restaurantDAO.updateRestaurant(restaurant, restaurantId);
    }

    public double getMenuItemPrice(int restaurantId, String itemName) throws SQLException {
        Restaurant restaurant = getRestaurantById(restaurantId);
        Double price = restaurant.getMenu().get(itemName);
        if (price == null) {
            throw new EntityNotFoundException("MenuItem", itemName);
        }
        return price;
    }
}