package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ParkingSlotRepositoryTest {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {

        entityManager.clear();

        ParkingSlot slot1 = new ParkingSlot(1L, VehicleType.BIKE, 1L, SlotStatus.AVAILABLE);
        ParkingSlot slot2 = new ParkingSlot(2L, VehicleType.CAR, 1L, SlotStatus.AVAILABLE);
        ParkingSlot slot3 = new ParkingSlot(3L, VehicleType.TRUCK, 2L, SlotStatus.AVAILABLE);
        ParkingSlot slot4 = new ParkingSlot(4L, VehicleType.CAR, 2L, SlotStatus.OCCUPIED);
        ParkingSlot slot5 = new ParkingSlot(5L, VehicleType.CAR, 3L, SlotStatus.AVAILABLE);

        parkingSlotRepository.saveAll(List.of(slot1, slot2, slot3, slot4, slot5));
    }

    @AfterEach
    void deleteAll() {
        parkingSlotRepository.deleteAll();
    }


    @Test
    void countByStatus_shouldReturnCorrectCountForAvailableSlots() {
        long count = parkingSlotRepository.countByStatus(SlotStatus.AVAILABLE);
        assertEquals(29, count);
    }

    @Test
    void countByStatus_shouldReturnCorrectCountForOccupiedSlots() {
        long count = parkingSlotRepository.countByStatus(SlotStatus.OCCUPIED);
        assertEquals(1, count);
    }


    @Test
    void findFirstAvailableSlot_shouldReturnFirstAvailableCarSlot() {
        Optional<ParkingSlot> result = parkingSlotRepository.findFirstAvailableSlot(SlotStatus.AVAILABLE, VehicleType.CAR);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
    }

    @Test
    void findFirstAvailableSlot_shouldReturnFirstAvailableTruckSlot() {
        Optional<ParkingSlot> result = parkingSlotRepository.findFirstAvailableSlot(SlotStatus.AVAILABLE, VehicleType.TRUCK);
        assertTrue(result.isPresent());
        assertEquals(3L, result.get().getId());
    }

    @Test
    void findFirstAvailableSlot_shouldRNotReturnEmpty_ifCompatibleSlotAvailable() {
        Optional<ParkingSlot> result = parkingSlotRepository.findFirstAvailableSlot(SlotStatus.OCCUPIED, VehicleType.CAR);
        assertTrue(result.isPresent());
    }

    @Test
    void findAllAvailableSlotsLevelWise_shouldReturnSlotsOrderedByFloor() {
        List<ParkingSlot> result = parkingSlotRepository.findAllAvailableSlotsLevelWise(SlotStatus.AVAILABLE, VehicleType.CAR);
        assertFalse(result.isEmpty());
        assertEquals(23, result.size());
        assertEquals(6L, result.get(0).getId()); // Floor 1
        assertEquals(7L, result.get(1).getId()); // Floor 2
        assertEquals(8L, result.get(2).getId()); // Floor 3
    }

    @Test
    void findAllAvailableSlotsWithLock_shouldReturnAllAvailableCompatibleSlots() {
        List<ParkingSlot> result = parkingSlotRepository.findAllAvailableSlotsWithLock(SlotStatus.AVAILABLE, VehicleType.BIKE);
        assertFalse(result.isEmpty());
        assertEquals(29, result.size());
    }
}