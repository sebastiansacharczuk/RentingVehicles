package org.example;

import java.util.List;

public interface IVehicleRepository {
    boolean rentVehicle(int id);
    Vehicle returnVehicle(int id);
    List<Vehicle> getVehicles();

    void save();
    void load(String fileName);
}
