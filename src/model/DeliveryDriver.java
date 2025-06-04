package model;

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