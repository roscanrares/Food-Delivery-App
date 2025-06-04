package model;

class PremiumUser extends User {
    private double discount;

    public PremiumUser(String name, String address, double initialBalance, double discount) {
        super(name, address, initialBalance);
        this.discount = discount;
    }

    public double getDiscount() { return discount; }
}