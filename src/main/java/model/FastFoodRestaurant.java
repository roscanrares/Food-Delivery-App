package model;

public class FastFoodRestaurant extends Restaurant {
    private int averageServeTime; // in minute

    public FastFoodRestaurant(String name) {
        super(name, "Fast-Food");
        this.averageServeTime = 7; // implicit, fast-food-ul servește rapid
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Meniu zilnic recomandat", 34.99);
    }

    // Functionalitate specifică: Happy hour (reducere la toate produsele)
    public void applyHappyHourDiscount(double procentReducere) {
        for (String item : menu.keySet()) {
            double pret = menu.get(item);
            menu.put(item, pret * (1 - procentReducere / 100));
        }
    }

    public int getAverageServeTime() {
        return averageServeTime;
    }

    public void setAverageServeTime(int averageServeTime) {
        this.averageServeTime = averageServeTime;
    }
}