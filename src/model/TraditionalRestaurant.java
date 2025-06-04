package model;

class TraditionalRestaurant extends Restaurant {
    private String chefSpecialty;

    public ClassicRestaurant(String name, String chefSpecialty) {
        super(name, "Traditional Clasic");
        this.chefSpecialty = chefSpecialty;
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Specialitatea casei: " + chefSpecialty, 49.99);
    }
}