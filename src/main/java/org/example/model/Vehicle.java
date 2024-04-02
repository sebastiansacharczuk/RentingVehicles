package org.example.model;

public abstract class Vehicle {
    protected String brand;
    protected String model;
    protected int year;
    protected double price;
    public boolean rented;

    public int id;
    public static int idGen = 1;


    public Vehicle(String brand, String model, int year, double price, boolean rented) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
        this.id = idGen++;
    }
    public Vehicle(int id, String brand, String model, int year, double price, boolean rented) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
        this.id = id;
    }

    public String toCSV() {
        return String.format("%d;%s;%s;%d;%.2f;%b;", id, brand, model, year, price, rented);
    }

    public String toString() {
        return String.format("brand: %s model: %s year: %d price: %.2f rented: %b", brand, model, year, price, rented);
    }

}