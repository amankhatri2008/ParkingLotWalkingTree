package com.walkingtree.parkinglot.service;

import com.walkingtree.parkinglot.dto.ParkedVehicleInfo;
import com.walkingtree.parkinglot.dto.ParkingStatusResponse;
import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.TicketStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.model.PricingRule;
import com.walkingtree.parkinglot.model.Ticket;
import com.walkingtree.parkinglot.repository.ParkingSlotRepository;
import com.walkingtree.parkinglot.repository.PricingRuleRepository;
import com.walkingtree.parkinglot.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private ParkingSlotRepository parkingSlotRepository;
    @Mock
    private PricingRuleRepository pricingRuleRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private AdminService adminService;

    private ParkingSlot parkingSlot;
    private PricingRule pricingRule;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        parkingSlot = new ParkingSlot();
        parkingSlot.setId(1L);
        parkingSlot.setSlotType(VehicleType.CAR);
        parkingSlot.setStatus(SlotStatus.AVAILABLE);

        pricingRule = new PricingRule();
        pricingRule.setVehicleType(VehicleType.CAR);
        pricingRule.setBasePrice(10.0);
        pricingRule.setHourlyRate(5.0);

        ticket = new Ticket();
        ticket.setId(101L);
        ticket.setVehiclePlateNo("ABC-123");
        ticket.setParkingSlot(parkingSlot);
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setStatus(TicketStatus.ACTIVE);
    }

    @Test
    void addSlot_shouldSetStatusToAvailableAndSave() {

        ParkingSlot newSlot = new ParkingSlot();
        when(parkingSlotRepository.save(any(ParkingSlot.class))).thenReturn(newSlot);


        ParkingSlot result = adminService.addSlot(newSlot);


        assertEquals(SlotStatus.AVAILABLE, newSlot.getStatus());
        verify(parkingSlotRepository, times(1)).save(newSlot);
        assertNotNull(result);
    }


    @Test
    void removeSlot_shouldRemoveSlot_ifSlotIsAvailable() {

        when(parkingSlotRepository.findById(1L)).thenReturn(Optional.of(parkingSlot));
        parkingSlot.setStatus(SlotStatus.AVAILABLE);


        adminService.removeSlot(1L);


        verify(parkingSlotRepository, times(1)).delete(parkingSlot);
    }

    @Test
    void removeSlot_shouldThrowException_ifSlotIsOccupied() {

        when(parkingSlotRepository.findById(1L)).thenReturn(Optional.of(parkingSlot));
        parkingSlot.setStatus(SlotStatus.OCCUPIED);


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> adminService.removeSlot(1L));
        assertEquals("Cannot remove an occupied slot.", exception.getMessage());
        verify(parkingSlotRepository, never()).delete(any(ParkingSlot.class));
    }

    @Test
    void removeSlot_shouldThrowException_ifSlotNotFound() {

        when(parkingSlotRepository.findById(99L)).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.removeSlot(99L));
        assertEquals("Parking slot with ID 99 not found.", exception.getMessage());
        verify(parkingSlotRepository, never()).delete(any(ParkingSlot.class));
    }


    @Test
    void addPricingRule_shouldUpdateExistingRule() {

        PricingRule updatedRule = new PricingRule();
        updatedRule.setVehicleType(VehicleType.CAR);
        updatedRule.setBasePrice(15.0);
        updatedRule.setHourlyRate(7.5);

        when(pricingRuleRepository.findByVehicleType(VehicleType.CAR)).thenReturn(Optional.of(pricingRule));
        when(pricingRuleRepository.save(any(PricingRule.class))).thenReturn(updatedRule);


        PricingRule result = adminService.addPricingRule(updatedRule);


        assertEquals(15.0, result.getBasePrice());
        assertEquals(7.5, result.getHourlyRate());
        verify(pricingRuleRepository, times(1)).save(pricingRule);
    }

    @Test
    void addPricingRule_shouldAddNewRule_ifDoesNotExist() {

        PricingRule newRule = new PricingRule();
        newRule.setVehicleType(VehicleType.BIKE);
        newRule.setBasePrice(5.0);
        newRule.setHourlyRate(2.5);

        when(pricingRuleRepository.findByVehicleType(VehicleType.BIKE)).thenReturn(Optional.empty());
        when(pricingRuleRepository.save(any(PricingRule.class))).thenReturn(newRule);


        PricingRule result = adminService.addPricingRule(newRule);


        assertEquals(VehicleType.BIKE, result.getVehicleType());
        verify(pricingRuleRepository, times(1)).save(newRule);
    }


    @Test
    void getParkingStatus_shouldReturnCorrectStatus() {

        long totalSlots = 10L;
        long availableSlots = 7L;
        long occupiedSlots = 3L;

        when(parkingSlotRepository.count()).thenReturn(totalSlots);
        when(parkingSlotRepository.countByStatus(SlotStatus.AVAILABLE)).thenReturn(availableSlots);
        when(ticketRepository.findAllByStatus(TicketStatus.ACTIVE)).thenReturn(Collections.singletonList(ticket));


        ParkingStatusResponse response = adminService.getParkingStatus();


        assertEquals(totalSlots, response.getTotalSlots());
        assertEquals(availableSlots, response.getAvailableSlots());
        assertEquals(occupiedSlots, response.getOccupiedSlots());
        assertFalse(response.getParkedVehicles().isEmpty());


        ParkedVehicleInfo info = response.getParkedVehicles().get(0);
        assertEquals(ticket.getId(), info.getTicketId());
        assertEquals(ticket.getVehiclePlateNo(), info.getPlateNo());
        assertEquals(ticket.getParkingSlot().getId(), info.getSlotId());
        assertEquals(ticket.getEntryTime(), info.getEntryTime());
    }

    @Test
    void getParkingStatus_shouldReturnEmptyList_ifNoVehiclesAreParked() {

        long totalSlots = 10L;
        long availableSlots = 10L;
        long occupiedSlots = 0L;

        when(parkingSlotRepository.count()).thenReturn(totalSlots);
        when(parkingSlotRepository.countByStatus(SlotStatus.AVAILABLE)).thenReturn(availableSlots);
        when(ticketRepository.findAllByStatus(TicketStatus.ACTIVE)).thenReturn(Collections.emptyList());


        ParkingStatusResponse response = adminService.getParkingStatus();

        assertEquals(totalSlots, response.getTotalSlots());
        assertEquals(availableSlots, response.getAvailableSlots());
        assertEquals(occupiedSlots, response.getOccupiedSlots());
        assertTrue(response.getParkedVehicles().isEmpty());
    }
}