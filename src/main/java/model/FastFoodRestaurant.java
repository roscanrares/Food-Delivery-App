package model;

class FastFoodRestaurant extends Restaurant {
    public FastFoodRestaurant(String name) {
        super(name, "Fast-Food");
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Meniu zilnic recomandat", 34.99);
    }
}