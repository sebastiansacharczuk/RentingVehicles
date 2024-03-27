package org.example;

import java.util.List;

public interface IVehicleRepository {
    boolean rentVehicle(int id);
    Vehicle getVehicle(int id);
    List<Vehicle> getVehicles();

    void addVehicle(Vehicle vehicle);
    boolean removeVehicle(int id);

    void save();
    void load();
}
