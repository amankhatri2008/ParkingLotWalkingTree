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
import java.util.Random;

@Component("randomStrategy")
public class RandomAvailableStrategy implements SlotAllocationStrategy {

    private final ParkingSlotRepository parkingSlotRepository;
    private final Random random = new Random();

    public RandomAvailableStrategy(ParkingSlotRepository parkingSlotRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
    }

    @Override
    @Transactional
    public Optional<ParkingSlot> findSlot(VehicleType vehicleType) {
        List<ParkingSlot> availableSlots = parkingSlotRepository.findAllAvailableSlotsWithLock(
                SlotStatus.AVAILABLE,
                vehicleType
        );

        if (availableSlots.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt(availableSlots.size());
        return Optional.of(availableSlots.get(randomIndex));
    }
}