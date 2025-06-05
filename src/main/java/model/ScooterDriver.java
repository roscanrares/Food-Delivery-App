package model;

public class ScooterDriver extends DeliveryDriver {
    private String licensePlate;

    public ScooterDriver(String name, String licensePlate) {
        super(name, "Scuter", 2); // tip vehicul și capacitate maximă
        this.licensePlate = licensePlate;
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        // Viteza medie: 35 km/h -> timp în minute
        return (distance / 35) * 60;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    // Opțional: setter
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}