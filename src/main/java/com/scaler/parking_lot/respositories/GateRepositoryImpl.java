package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Gate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GateRepositoryImpl implements GateRepository{
    private List<Gate> gates = new ArrayList<>();
    private static long idCounter = 0;
    @Override
    public Optional<Gate> findById(long gateId) {
        return gates.stream().filter(gate -> gate.getId()==gateId).findFirst();
    }

    @Override
    public Gate save(Gate gate) {
        if(gate.getId()==0)
            gate.setId(++idCounter);
        gates.add(gate);
        return gate;
    }
}
