package model;

public class Bakery extends Restaurant {
    public Bakery(String name) {
        super(name, "Bakery");
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        // Exemplu: adaugă o prăjitură specială
        addMenuItem("Cozonac tradițional", 17.99);
    }

    // Funcționalitate specifică Bakery (exemplu)
    public void addPastryOfTheDay(String pastry, double price) {
        addMenuItem("Specialitatea zilei: " + pastry, price);
    }
}