package com.walkingtree.parkinglot.strategy.Impl;

import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.repository.ParkingSlotRepository;
import com.walkingtree.parkinglot.strategy.SlotAllocationStrategy;
import com.walkingtree.parkinglot.vehicles.IVehicle;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.walkingtree.parkinglot.vehicles.IVehicle.getCompatibleSlotTypes;

@Component("levelWiseStrategy")
public class LevelWiseStrategy implements SlotAllocationStrategy {

    private final ParkingSlotRepository parkingSlotRepository;

    public LevelWiseStrategy(ParkingSlotRepository parkingSlotRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
    }

    @Override
    @Transactional
    public Optional<ParkingSlot> findSlot(IVehicle vehicle) {
        String vehicleType = vehicle.getClass().getSimpleName().toUpperCase();

        List<String> compatibleSlotTypes = getCompatibleSlotTypes(vehicleType);
        List<ParkingSlot> availableSlots = parkingSlotRepository.findAllAvailableSlotsLevelWise(
                SlotStatus.AVAILABLE,
                compatibleSlotTypes
        );

        return availableSlots.stream().findFirst();
    }
}