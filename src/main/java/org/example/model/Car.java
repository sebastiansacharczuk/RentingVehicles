package org.example.model;

public class Car extends Vehicle {
    public Car(String brand, String model, int year, double price, boolean rented) {
        super(brand, model, year, price, rented);
    }
    public Car(int id, String brand, String model, int year, double price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    @Override
    public String toCSV() {
        return "C;" + super.toCSV();
    }
}
