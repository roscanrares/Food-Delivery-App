package model;

public class CarDriver extends DeliveryDriver {
    private String carModel;

    public CarDriver(String name, String carModel) {
        super(name, "Masina", 5); // tip vehicul și capacitate maximă
        this.carModel = carModel;
    }

    @Override
    public double calculateDeliveryTime(double distance) {
        // Viteza medie: 50 km/h -> timp în minute
        return (distance / 50) * 60;
    }

    public String getCarModel() {
        return carModel;
    }

    // Opțional: setter
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}