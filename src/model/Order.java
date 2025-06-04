package model;

class Order {
    private User user;
    private Restaurant restaurant;
    private List<String> items;
    private double totalPrice;
    private DeliveryDriver driver;
    private boolean isPaid;

    public Order(User user, Restaurant restaurant, List<String> items, DeliveryDriver driver) {
        this.user = user;
        this.restaurant = restaurant;
        this.items = items;
        this.driver = driver;
        this.isPaid = false;
        calculateTotal();
    }

    private void calculateTotal() {
        totalPrice = items.stream()
                .mapToDouble(item -> restaurant.getMenu().getOrDefault(item, 0.0))
                .sum();

        if (user instanceof PremiumUser) {
            totalPrice *= (1 - ((PremiumUser) user).getDiscount());
        }
    }

    public void processPayment() {
        if (isPaid) {
            throw new IllegalStateException("Comanda a fost deja platita");
        }
        user.deductBalance(totalPrice);
        isPaid = true;
    }

    public double getTotalPrice() { return totalPrice; }
    public User getUser() { return user; }
    public Restaurant getRestaurant() { return restaurant; }
    public DeliveryDriver getDriver() { return driver; }
}