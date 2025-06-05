package model;
import exception.InsufficientFundsException;

public abstract class User {
    private String name;
    private String address;
    protected double balance;

    // Constructor protected (doar pentru subclase)
    protected User(String name, String address, double initialBalance) {
        this.name = name;
        this.address = address;
        this.balance = initialBalance;
    }

    public void deductBalance(double amount) {
        if (balance < amount) {
            throw new InsufficientFundsException("Fonduri insuficiente pentru " + name +
                    ". Mai aveti nevoie de " + (amount - balance) + " lei");
        }
        balance -= amount;
    }

    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie pozitiva");
        }
        balance += amount;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getBalance() { return balance; }

    // Poți adăuga și setteri dacă vrei să poți modifica name/address ulterior
}