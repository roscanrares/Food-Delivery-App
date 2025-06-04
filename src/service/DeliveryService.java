package service;

class DeliveryService {
    private List<User> users = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<DeliveryDriver> drivers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
    }

    public void addDriver(DeliveryDriver driver) {
        drivers.add(driver);
    }

    public DeliveryDriver assignDriver() {
        return drivers.stream()
                .filter(DeliveryDriver::isAvailable)
                .findFirst()
                .orElse(null);
    }

    public void completeDelivery(Order order) {
        order.getDriver().completeOrder();
        System.out.println("Comanda livrata de " + order.getDriver().getName() +
                ". Comenzi active: " + order.getDriver().getCurrentLoad());
    }

    public void placeOrder(String userName, String restaurantName, List<String> items) {
        User user = users.stream()
                .filter(u -> u.getName().equals(userName))
                .findFirst()
                .orElse(null);

        Restaurant restaurant = restaurants.stream()
                .filter(r -> r.getName().equals(restaurantName))
                .findFirst()
                .orElse(null);

        DeliveryDriver driver = assignDriver();

        if (user != null && restaurant != null && driver != null) {
            try {
                Order order = new Order(user, restaurant, items, driver);

                order.processPayment();
                driver.acceptOrder();
                orders.add(order);

                System.out.println("\nComanda plasata cu succes!");
                System.out.println("Client: " + user.getName());
                System.out.println("Sold ramas: " + user.getBalance() + " lei");
                System.out.println("Restaurant: " + restaurant.getName());
                System.out.println("Livrator: " + driver.getName());
                System.out.println("Total platit: " + order.getTotalPrice() + " lei");
            } catch (InsufficientFundsException e) {
                System.out.println("Eroare la plata: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        } else {
            System.out.println("Nu s-a putut plasa comanda. Verificati disponibilitatea!");
        }
    }

    public void displayMenu(String restaurantName) {
        restaurants.stream()
                .filter(r -> r.getName().equals(restaurantName))
                .findFirst()
                .ifPresent(r -> {
                    System.out.println("\nMeniu " + r.getType() + " " + r.getName());
                    System.out.println("----------------------------------");
                    r.getMenu().forEach((item, price) ->
                            System.out.printf("- %s: %.2f lei%n", item, price));
                });
    }
}