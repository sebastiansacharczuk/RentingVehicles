package org.example.controller;

import org.example.dto.RentVehicleDto;
import org.example.dto.ReturnVehicleDto;
import org.example.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rents")
public class RentController {

    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("/rent")
    public ResponseEntity<String> rentVehicle(@RequestBody RentVehicleDto request) {
        boolean success = rentService.rentVehicle(request.getPlate(),request.getLogin());
        if (success) {
            return ResponseEntity.ok("Vehicle rented");
        } else {
            return ResponseEntity.badRequest().body("Failed");
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnVehicle(@RequestBody ReturnVehicleDto request) {
        boolean success = rentService.returnVehicle(request.getLogin());
        if (success) {
            return ResponseEntity.ok("Vehicle returned");
        } else {
            return ResponseEntity.badRequest().body("Failed");
        }
    }
}