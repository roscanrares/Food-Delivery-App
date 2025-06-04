import java.util.*;

class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

abstract class User {
    private String name;
    private String address;
    protected double balance;

    public User(String name, String address, double initialBalance) {
        this.name = name;
        this.address = address;
        this.balance = initialBalance;
    }

    public void deductBalance(double amount) {
        if (balance < amount) {
            throw new InsufficientFundsException("Fonduri insuficiente pentru " + name +
                    ". Mai aveti nevoie de " + (amount - balance) + " lei");
        }
        balance -= amount;
    }

    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie pozitiva");
        }
        balance += amount;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getBalance() { return balance; }
}

class RegularUser extends User {
    public RegularUser(String name, String address, double initialBalance) {
        super(name, address, initialBalance);
    }
}

class PremiumUser extends User {
    private double discount;

    public PremiumUser(String name, String address, double initialBalance, double discount) {
        super(name, address, initialBalance);
        this.discount = discount;
    }

    public double getDiscount() { return discount; }
}

abstract class DeliveryDriver {
    private String name;
    private String vehicleType;
    private int currentLoad;
    private final int maxCapacity;

    public DeliveryDriver(String name, String vehicleType, int maxCapacity) {
        this.name = name;
        this.vehicleType = vehicleType;
        this.maxCapacity = maxCapacity;
        this.currentLoad = 0;
    }

    public abstract double calculateDeliveryTime(double distance);

    public boolean isAvailable() {
        return currentLoad < maxCapacity;
    }

    public void acceptOrder() {
        if (!isAvailable()) {
            throw new IllegalStateException("Livratorul a atins capacitatea maxima");
        }
        currentLoad++;
    }

    public void completeOrder() {
        if (currentLoad <= 0) {
            throw new IllegalStateException("Nu exista comenzi de finalizat");
        }
        currentLoad--;
    }

    public String getName() { return name; }
    public String getVehicleType() { return vehicleType; }
    public int getCurrentLoad() { return currentLoad; }
    public int getMaxCapacity() { return maxCapacity; }
}

class CarDriver extends DeliveryDriver {
    private String carModel;

    public CarDriver(String name, String carModel) {
        super(name, "Masina", 5);
        this.carModel = carModel;
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return (distance / 50) * 60;
    }

    public String getCarModel() { return carModel; }
}

class ScooterDriver extends DeliveryDriver {
    private String licensePlate;

    public ScooterDriver(String name, String licensePlate) {
        super(name, "Scuter", 2);
        this.licensePlate = licensePlate;
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        return (distance / 35) * 60;
    }

    public String getLicensePlate() { return licensePlate; }
}

abstract class Restaurant {
    private String name;
    protected TreeMap<String, Double> menu;
    private String type;

    public Restaurant(String name, String type) {
        this.name = name;
        this.menu = new TreeMap<>();
        this.type = type;
    }

    public abstract void addSpecialItem();

    public void addMenuItem(String item, double price) {
        menu.put(item, price);
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public TreeMap<String, Double> getMenu() { return menu; }
}

class ClassicRestaurant extends Restaurant {
    private String chefSpecialty;

    public ClassicRestaurant(String name, String chefSpecialty) {
        super(name, "Restaurant Clasic");
        this.chefSpecialty = chefSpecialty;
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Specialitatea casei: " + chefSpecialty, 49.99);
    }
}

class Bakery extends Restaurant {
    public Bakery(String name) {
        super(name, "Brutarie");
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Pachet zilnic de produse proaspete", 25.99);
    }
}

class FastFoodRestaurant extends Restaurant {
    public FastFoodRestaurant(String name) {
        super(name, "Fast-Food");
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Meniu zilnic recomandat", 34.99);
    }
}

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

class FoodDeliveryPlatform {
    public static void main(String[] args) {

        List<User> users = new ArrayList<>();
        users.add(new PremiumUser("Maria Ionescu", "Bulevardul Libertatii 25", 150.0, 0.15));
        users.add(new RegularUser("Ion Popescu", "Strada Principala 10", 100.0));
        users.add(new PremiumUser("Ana Maria", "Strada Florilor 5", 200.0, 0.10));
        users.add(new RegularUser("George Vasilescu", "Bulevardul Unirii 15", 50.0));



        for (User user : users) {
            System.out.println(user.getName());

        }
        System.out.println();

        users.sort(Comparator.comparingDouble(User::getBalance));

        // Print sorted users
        for (User user : users) {
            System.out.println(user.getName() + "  " + user.getBalance());
        }



//        DeliveryService service = new DeliveryService();
//
//        service.addUser(new RegularUser("Ion Popescu", "Strada Principala 10", 100.0));
//        service.addUser(new PremiumUser("Maria Ionescu", "Bulevardul Libertatii 25", 150.0, 0.15));
//
//        ClassicRestaurant restaurant1 = new ClassicRestaurant("La Mosulet", "Friptura de miel");
//        restaurant1.addMenuItem("Ciorba de fasole", 18.99);
//        service.addRestaurant(restaurant1);
//
//        service.addDriver(new CarDriver("Andrei Georgescu", "Dacia Logan"));
//        service.addDriver(new ScooterDriver("Scooter", "Honda SH125"));
//        service.assignDriver();
//
//        try {
//            service.placeOrder("Maria Ionescu", "La Mosulet",
//                    List.of("Ciorba de fasole", "Specialitatea casei: Friptura de miel"));
//
//            service.placeOrder("Ion Popescu", "La Moșulet",
//                    List.of("Specialitatea casei: Friptura de miel", "Ciorba de fasole"));
//        } catch (Exception e) {
//            System.out.println("Eroare neașteptata: " + e.getMessage());
//        }
//
//        User ion = service.getUsers().stream()
//                .filter(u -> u.getName().equals("Ion Popescu"))
//                .findFirst()
//                .orElse(null);
//
//        if (ion != null) {
//            ion.addFunds(50.0);
//            service.placeOrder("Ion Popescu", "La Moșulet",
//                    List.of("Specialitatea casei: Friptura de miel"));
//        }
    }
}