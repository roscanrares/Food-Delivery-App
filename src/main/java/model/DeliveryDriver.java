package model;

import exception.DriverCapacityExceededException;

public abstract class DeliveryDriver {
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

    // Metodă abstractă pentru calculul timpului de livrare (implementată în subclase)
    public abstract double calculateDeliveryTime(double distance);

    // Verifică dacă livratorul poate prelua o comandă nouă
    public boolean isAvailable() {
        return currentLoad < maxCapacity;
    }

    // Acceptă o comandă nouă (crește sarcina curentă)
    public void acceptOrder() {
        if (!isAvailable()) {
            throw new DriverCapacityExceededException(getName());
        }
        currentLoad++;
    }

    // Marchează o comandă ca finalizată (scade sarcina curentă)
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