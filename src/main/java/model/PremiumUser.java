package model;

public class PremiumUser extends User {
    private double discount;

    public PremiumUser(String name, String address, double initialBalance, double discount) {
        super(name, address, initialBalance);
        this.discount = discount;
    }

    public double getDiscount() { return discount; }

    // Opțional
    public void setDiscount(double discount) {
        this.discount = discount;
    }
}