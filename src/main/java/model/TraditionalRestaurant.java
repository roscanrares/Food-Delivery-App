package model;

public class TraditionalRestaurant extends Restaurant {
    private String chefSpecialty;

    public TraditionalRestaurant(String name, String chefSpecialty) {
        super(name, "Traditional Clasic");
        this.chefSpecialty = chefSpecialty;
        addSpecialItem();
    }

    @Override
    public void addSpecialItem() {
        addMenuItem("Specialitatea casei: " + chefSpecialty, 49.99);
    }

    // Funcționalitate specifică: schimbă specialitatea
    public void setChefSpecialty(String chefSpecialty) {
        this.chefSpecialty = chefSpecialty;
        // Poți reface specialitatea în meniu dacă vrei
        // Sau poți adăuga o nouă specialitate în meniu
    }

    public String getChefSpecialty() {
        return chefSpecialty;
    }
}