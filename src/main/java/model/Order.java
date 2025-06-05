package model;

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
                        throw new IllegalArgumentException("Itemul " + item + " nu există în meniu");
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
            throw new IllegalStateException("Comanda a fost deja platita");
        }
        if (user.getBalance() < totalPrice) {
            throw new IllegalStateException("Fonduri insuficiente");
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