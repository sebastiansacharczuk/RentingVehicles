package org.example.service;

import org.example.dao.IVehicleRepository;
import org.example.dto.CreateVehicleDto;
import org.example.dto.VehicleDto;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class VehicleService {

    private IVehicleRepository vehicleRepository;
    @Autowired
    public VehicleService(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Collection<VehicleDto> getVehicles() {
        Collection<VehicleDto> vehicleDtos = new ArrayList<>();
        Collection<Vehicle> vehicles = vehicleRepository.getVehicles();
        for (Vehicle vehicle : vehicles) {
            String category = null;
            if(vehicle instanceof Motorcycle){
                category = ((Motorcycle) vehicle).getCategory();
            }
            VehicleDto vehicleDto = new VehicleDto(
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getPrice(),
                    vehicle.getPlate(),
                    vehicle.isRent(),
                    category,
                    vehicle.getUserLogin(),
                    vehicle.getUserRole()
            );
            vehicleDtos.add(vehicleDto);
        }
        return vehicleDtos;
    }

    public VehicleDto getVehicle(String plate) {
        System.out.println("plate");
        Vehicle vehicle = vehicleRepository.getVehicle(plate);
        System.out.println("plate");
        if (vehicle != null){
            String category = null;
            if(vehicle instanceof Motorcycle){
                category = ((Motorcycle) vehicle).getCategory();
            }
            return new VehicleDto(
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getPrice(),
                    vehicle.getPlate(),
                    vehicle.isRent(),
                    category,
                    vehicle.getUserLogin(),
                    vehicle.getUserRole()
            );
        }
        else
            return null;
    }

    public boolean createVehicle(CreateVehicleDto createVehicleDto) {

        Vehicle newVehicle = null;
        if (!createVehicleDto.getCategory().isEmpty()){
            newVehicle = new Motorcycle();
        }
        else {
            newVehicle = new Car();
        }

        newVehicle.setBrand(createVehicleDto.getBrand());
        newVehicle.setModel(createVehicleDto.getModel());
        newVehicle.setPlate(createVehicleDto.getPlate());
        newVehicle.setPrice(createVehicleDto.getPrice());
        newVehicle.setYear(createVehicleDto.getYear());
        newVehicle.setRent(false);
        if ( newVehicle instanceof Motorcycle ){
            ((Motorcycle) newVehicle).setCategory(createVehicleDto.getCategory());
        }
        return vehicleRepository.addVehicle(newVehicle);
    }
    public String deleteVehicle(String plate) {
        Vehicle vehicle = vehicleRepository.getVehicle(plate);
        if (vehicle == null)
            return "not found";
        else if (vehicle.getUser() != null)
            return "user is not null";
        else
            vehicleRepository.removeVehicle(plate);
        return "deleted";
    }

}


