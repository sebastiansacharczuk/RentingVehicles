package org.example.dao;

import org.example.model.Vehicle;

import java.util.Collection;
import java.util.List;

public interface IVehicleRepository {

    Collection<Vehicle> getVehicles();
    Vehicle getVehicle(String plate);

    boolean addVehicle(Vehicle vehicle);
    boolean removeVehicle(String plate);

    boolean rentVehicle(String plate,String login);
    boolean returnVehicle(String plate,String login );

}