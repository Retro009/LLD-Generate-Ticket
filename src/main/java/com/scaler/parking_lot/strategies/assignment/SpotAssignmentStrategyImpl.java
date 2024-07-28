package com.scaler.parking_lot.strategies.assignment;

import com.scaler.parking_lot.exceptions.ParkingSpotNotAvailableException;
import com.scaler.parking_lot.models.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpotAssignmentStrategyImpl implements SpotAssignmentStrategy{
    @Override
    public Optional<ParkingSpot> assignSpot(ParkingLot parkingLot, VehicleType vehicleType) {
        List<ParkingFloor> parkingFloors = parkingLot.getParkingFloors().stream().filter(floor -> floor.getStatus().equals(FloorStatus.OPERATIONAL)).collect(Collectors.toList());
        List<ParkingFloor> eligibleParkingFloors = parkingFloors.stream().filter(floor -> floor.getSpots().stream().filter(spot -> spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(vehicleType)).findAny().isPresent()).collect(Collectors.toList());
        if(eligibleParkingFloors.size()==0)
            return Optional.empty();

        eligibleParkingFloors.sort((floor1,floor2)->{
            int result = floor1.getSpots().stream().filter(spot -> spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(vehicleType)).collect(Collectors.toList()).size() - floor2.getSpots().stream().filter(spot-> spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(vehicleType)).collect(Collectors.toList()).size();
            if(result == 0)
                result = floor1.getFloorNumber() - floor2.getFloorNumber();
            return result;
        });

        List<ParkingSpot> spots = eligibleParkingFloors.get(0).getSpots();

        spots.sort(Comparator.comparingInt(ParkingSpot::getNumber));

        return spots.stream().filter(spot -> spot.getStatus().equals(ParkingSpotStatus.AVAILABLE) && spot.getSupportedVehicleType().equals(vehicleType)).findFirst();
    }
}
