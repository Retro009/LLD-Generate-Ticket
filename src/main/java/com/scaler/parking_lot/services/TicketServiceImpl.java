package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.InvalidGateException;
import com.scaler.parking_lot.exceptions.InvalidParkingLotException;
import com.scaler.parking_lot.exceptions.ParkingSpotNotAvailableException;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.GateRepository;
import com.scaler.parking_lot.respositories.ParkingLotRepository;
import com.scaler.parking_lot.respositories.TicketRepository;
import com.scaler.parking_lot.respositories.VehicleRepository;
import com.scaler.parking_lot.strategies.assignment.SpotAssignmentStrategy;

import java.util.Date;
import java.util.Optional;

public class TicketServiceImpl implements TicketService{
    GateRepository gateRepository;
    ParkingLotRepository parkingLotRepository;
    TicketRepository ticketRepository;
    VehicleRepository vehicleRepository;
    SpotAssignmentStrategy spotAssignmentStrategy;

    public TicketServiceImpl(GateRepository gateRepository,ParkingLotRepository parkingLotRepository,TicketRepository ticketRepository, VehicleRepository vehicleRepository, SpotAssignmentStrategy spotAssignmentStrategy){
        this.gateRepository = gateRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.ticketRepository = ticketRepository;
        this.vehicleRepository = vehicleRepository;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
    }
    @Override
    public Ticket generateTicket(int gateId, String registrationNumber, String vehicleType) throws InvalidGateException, InvalidParkingLotException, ParkingSpotNotAvailableException {
        Optional<Gate> optionalGate = gateRepository.findById(gateId);
        if(optionalGate.isEmpty() || optionalGate.get().getType().equals(GateType.EXIT))
            throw new InvalidGateException("Invalid Gate");
        Gate gate = optionalGate.get();
        Optional<Vehicle> optionalVehicle = vehicleRepository.getVehicleByRegistrationNumber(registrationNumber);
        Vehicle vehicle;
        if(optionalVehicle.isEmpty()){
            vehicle = new Vehicle();
            vehicle.setVehicleType(VehicleType.valueOf(vehicleType));
            vehicle.setRegistrationNumber(registrationNumber);
            vehicleRepository.save(vehicle);
        }else
            vehicle = optionalVehicle.get();
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByGateId(gateId).orElseThrow(() -> new InvalidParkingLotException("Invalid Gate"));

        ParkingSpot parkingSpot = spotAssignmentStrategy.assignSpot(parkingLot, VehicleType.valueOf(vehicleType)).orElseThrow(()-> new ParkingSpotNotAvailableException("Parking Spot Not Available!"));

        Ticket ticket = new Ticket();
        ticket.setGate(gate);
        ticket.setVehicle(vehicle);
        ticket.setEntryTime(new Date());
        ticket.setParkingSpot(parkingSpot);
        ticket.setParkingAttendant(gate.getParkingAttendant());
        return ticket;
    }
}
