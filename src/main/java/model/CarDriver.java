package model;

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