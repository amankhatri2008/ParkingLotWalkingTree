package com.walkingtree.parkinglot.strategy;

import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.vehicles.IVehicle;

import java.util.Optional;

public interface SlotAllocationStrategy {
    Optional<ParkingSlot> findSlot(IVehicle vehicleType);
}