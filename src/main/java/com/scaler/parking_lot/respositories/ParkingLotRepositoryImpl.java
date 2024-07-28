package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.ParkingLot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingLotRepositoryImpl implements ParkingLotRepository{
    private List<ParkingLot> parkingLots = new ArrayList<>();
    private static long idCounter = 0;
    @Override
    public Optional<ParkingLot> getParkingLotByGateId(long gateId) {
        return parkingLots.stream().filter(lot -> lot.getGates().stream().filter(gate -> gate.getId()==gateId).findFirst().isPresent()).findFirst();
    }

    @Override
    public Optional<ParkingLot> getParkingLotById(long id) {
        return parkingLots.stream().filter(lot -> lot.getId()==id).findFirst();
    }

    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        if(parkingLot.getId()==0)
            parkingLot.setId(++idCounter);
        parkingLots.add(parkingLot);
        return parkingLot;
    }
}
