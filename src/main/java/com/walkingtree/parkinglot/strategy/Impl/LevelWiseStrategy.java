package com.walkingtree.parkinglot.strategy.Impl;

import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.repository.ParkingSlotRepository;
import com.walkingtree.parkinglot.strategy.SlotAllocationStrategy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("levelWiseStrategy")
public class LevelWiseStrategy implements SlotAllocationStrategy {

    private final ParkingSlotRepository parkingSlotRepository;

    public LevelWiseStrategy(ParkingSlotRepository parkingSlotRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
    }

    @Override
    @Transactional
    public Optional<ParkingSlot> findSlot(VehicleType vehicleType) {
        List<ParkingSlot> availableSlots = parkingSlotRepository.findAllAvailableSlotsLevelWise(
                SlotStatus.AVAILABLE,
                vehicleType
        );

        return availableSlots.stream().findFirst();
    }
}