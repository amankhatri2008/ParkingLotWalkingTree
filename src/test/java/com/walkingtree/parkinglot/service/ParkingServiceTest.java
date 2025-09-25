package com.walkingtree.parkinglot.service;

import com.walkingtree.parkinglot.dto.ReceiptDetails;
import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.TicketStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.*;
import com.walkingtree.parkinglot.repository.*;
import com.walkingtree.parkinglot.strategy.SlotAllocationStrategy;
import com.walkingtree.parkinglot.vehicles.IVehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PricingRuleRepository pricingRuleRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private SlotAllocationStrategy slotAllocationStrategy;
    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @InjectMocks
    private ParkingService parkingService;

    private Vehicle vehicle;
    private ParkingSlot parkingSlot;
    private Ticket activeTicket;
    private PricingRule pricingRule;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setPlateNo("ABC-123");
        vehicle.setVehicleType(VehicleType.CAR);

        parkingSlot = new ParkingSlot();
        parkingSlot.setId(1L);
        parkingSlot.setSlotType(VehicleType.CAR);
        parkingSlot.setStatus(SlotStatus.AVAILABLE);

        activeTicket = new Ticket();
        activeTicket.setId(101L);
        activeTicket.setVehiclePlateNo("ABC-123");
        activeTicket.setParkingSlot(parkingSlot);
        activeTicket.setEntryTime(LocalDateTime.now().minusHours(2)); // 2 hours duration
        activeTicket.setStatus(TicketStatus.ACTIVE);

        pricingRule = new PricingRule();
        pricingRule.setBasePrice(5.0);
        pricingRule.setHourlyRate(3.0);
    }


    @Test
    void parkVehicle_shouldParkNewVehicleAndCreateTicket() {

        when(vehicleRepository.findByPlateNo(anyString())).thenReturn(Optional.empty());
        when(slotAllocationStrategy.findSlot(any(IVehicle.class))).thenReturn(Optional.of(parkingSlot));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(activeTicket);


        Ticket result = parkingService.parkVehicle("XYZ-789", VehicleType.CAR);


        assertNotNull(result);
        assertEquals(activeTicket, result);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        assertEquals(SlotStatus.OCCUPIED, parkingSlot.getStatus());
    }

    @Test
    void parkVehicle_shouldThrowException_ifVehicleAlreadyParked() {

        when(vehicleRepository.findByPlateNo(anyString())).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByVehiclePlateNoAndStatus(anyString(), any(TicketStatus.class))).thenReturn(Optional.of(activeTicket));


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> parkingService.parkVehicle("ABC-123", VehicleType.CAR));
        assertEquals("Vehicle with plate number ABC-123 is already parked.", exception.getMessage());
        verify(slotAllocationStrategy, never()).findSlot(any());
    }

    @Test
    void parkVehicle_shouldThrowException_ifNoAvailableSlots() {

        when(vehicleRepository.findByPlateNo(anyString())).thenReturn(Optional.empty());
        when(slotAllocationStrategy.findSlot(any(IVehicle.class))).thenReturn(Optional.empty());


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> parkingService.parkVehicle("XYZ-789", VehicleType.CAR));
        assertEquals("Parking lot is full for vehicle type CAR", exception.getMessage());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }


    @Test
    void exitVehicle_shouldExitVehicleAndCalculateCost() {

        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(activeTicket));
        when(pricingRuleRepository.findByVehicleType(any(VehicleType.class))).thenReturn(Optional.of(pricingRule));
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());


        ReceiptDetails receipt = parkingService.exitVehicle(101L);


        assertNotNull(receipt);
        assertEquals(activeTicket.getId(), receipt.getTicketId());
        assertEquals("ABC-123", receipt.getPlateNo());
        assertEquals(11.0, receipt.getTotalAmount()); // 5 (base) + (2 * 3) = 11
        assertEquals(TicketStatus.PAID, activeTicket.getStatus());
        assertEquals(SlotStatus.AVAILABLE, parkingSlot.getStatus());
        verify(ticketRepository, times(1)).save(activeTicket);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void exitVehicle_shouldThrowException_ifTicketNotFound() {

        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parkingService.exitVehicle(999L));
        assertEquals("Ticket with ID 999 not found.", exception.getMessage());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}