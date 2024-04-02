package org.example.model;

public class Motorcycle extends Vehicle {

    private final String category;

    @Override
    public String toCSV() {
        return "M;" + super.toCSV() + String.format("%s;", this.category);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" category: %s", this.category);
    }

    public Motorcycle(String brand, String model, int year, double price, boolean rented, String category) {
        super(brand, model, year, price, rented);
        this.category = category;
    }
    public Motorcycle(int id, String brand, String model, int year, double price, boolean rented, String category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }
}