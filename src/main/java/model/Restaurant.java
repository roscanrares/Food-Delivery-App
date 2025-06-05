package model;

import java.util.TreeMap;

public abstract class Restaurant {
    private String name;
    protected TreeMap<String, Double> menu;
    private String type;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant(String name, String type) {
        this.name = name;
        this.menu = new TreeMap<>();
        this.type = type;
    }

    public abstract void addSpecialItem();

    public void addMenuItem(String item, double price) {
        menu.put(item, price);
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public TreeMap<String, Double> getMenu() { return menu; }
}