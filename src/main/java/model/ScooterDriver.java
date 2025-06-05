package model;

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