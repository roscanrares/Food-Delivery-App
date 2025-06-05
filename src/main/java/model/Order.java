package model;

import exception.MenuItemNotFoundException;
import exception.OrderAlreadyPaidException;
import exception.InsufficientBalanceException;
import java.util.Collections;
import java.util.List;

public class Order {
    private final User user;
    private final Restaurant restaurant;
    private final List<String> items;
    private final DeliveryDriver driver;
    private double totalPrice;
    private boolean isPaid;

    public Order(User user, Restaurant restaurant, List<String> items, DeliveryDriver driver) {
        this.user = user;
        this.restaurant = restaurant;
        this.items = List.copyOf(items); // defensive copy, imutabil
        this.driver = driver;
        this.isPaid = false;
        calculateTotal();
    }

    private void calculateTotal() {
        totalPrice = items.stream()
                .mapToDouble(item -> {
                    Double price = restaurant.getMenu().get(item);
                    if (price == null) {
                        throw new MenuItemNotFoundException(item);
                    }
                    return price;
                })
                .sum();

        if (user instanceof PremiumUser) {
            totalPrice *= (1 - ((PremiumUser) user).getDiscount());
        }
    }

    public void processPayment() {
        if (isPaid) {
            throw new OrderAlreadyPaidException();
        }
        if (user.getBalance() < totalPrice) {
            throw new InsufficientBalanceException(totalPrice, user.getBalance());
        }
        user.deductBalance(totalPrice);
        isPaid = true;
    }

    public double getTotalPrice() { return totalPrice; }
    public User getUser() { return user; }
    public Restaurant getRestaurant() { return restaurant; }
    public DeliveryDriver getDriver() { return driver; }
    public List<String> getItems() { return Collections.unmodifiableList(items); }
    public boolean isPaid() { return isPaid; }

    @Override
    public String toString() {
        return "Order{" +
                "user=" + user.getName() +
                ", restaurant=" + restaurant.getName() +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", driver=" + driver.getName() +
                ", isPaid=" + isPaid +
                '}';
    }
}