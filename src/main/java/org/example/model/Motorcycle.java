package org.example.model;

public class Motorcycle extends Vehicle {

    private final String category;
    public Motorcycle(String brand, String model, int year, double price, String plate, String category) {
        super(brand, model, year, price, plate);
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "Motorcycle{" +
                super.toString()+
                " category='" + category + '\'' +
                '}';
    }

}