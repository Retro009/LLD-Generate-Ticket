package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleRepositoryImpl implements VehicleRepository{
    private List<Vehicle> vehicles = new ArrayList<>();
    private static long idCounter = 0;

    @Override
    public Optional<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return vehicles.stream().filter(vehicle -> vehicle.getRegistrationNumber().equals(registrationNumber)).findFirst();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if(vehicle.getId()==0)
            vehicle.setId(++idCounter);
        vehicles.add(vehicle);
        return vehicle;
    }
}
