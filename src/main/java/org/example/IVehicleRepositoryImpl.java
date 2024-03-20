package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IVehicleRepositoryImpl implements IVehicleRepository {
    private List<Vehicle> vehicles;
    private final String filename;
    public IVehicleRepositoryImpl(String filename) {
        this.vehicles = new ArrayList<>();
        this.filename = filename;
    }
    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        save();
    }

    public void removeVehicle(int id) {
        vehicles.removeIf(vehicle -> vehicle.id == id);
    }
    @Override
    public boolean rentVehicle(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.id == id && !vehicle.rented) {
                vehicle.rented = true;
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public Vehicle returnVehicle(int id) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.id == id) {
                vehicle.rented = true;
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");

                if (fields.length > 0) {
                    for (int i = 0; i < fields.length; i++) {
                        if(Objects.equals(fields[i], "C")){
                            Car newCar = new Car(Integer.parseInt(fields[i+1]), fields[i+2], fields[i+3], Integer.parseInt(fields[i+4]), Double.parseDouble(fields[i+5]), Boolean.parseBoolean(fields[i+6]));
                            vehicles.add(newCar);
                            i+=6;
                        }
                        else {
                            Motorcycle newMotorcycle = new Motorcycle(Integer.parseInt(fields[i+1]), fields[i+2], fields[i+3], Integer.parseInt(fields[i+4]), Double.parseDouble(fields[i+5]), Boolean.parseBoolean(fields[i+6]), fields[i+7]);
                            vehicles.add(newMotorcycle);
                            i+=7;
                        }
                    }
                }
            }
            Vehicle.idGen = vehicles.size()+1;
            reader.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Override
    public void save() {
        try {
            StringBuilder vehiclesStr = new StringBuilder();
            for (Vehicle vehicle : vehicles) {
                vehiclesStr.append(vehicle.toCSV());
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
            writer.write(String.valueOf(vehiclesStr));

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}