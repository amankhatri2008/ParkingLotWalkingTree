package com.walkingtree.parkinglot.service;

import com.walkingtree.parkinglot.dto.ReceiptDetails;
import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.TicketStatus;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.*;
import com.walkingtree.parkinglot.repository.*;
import com.walkingtree.parkinglot.strategy.SlotAllocationStrategy;
import com.walkingtree.parkinglot.vehicles.IVehicle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ParkingService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final VehicleRepository vehicleRepository;
    private final TicketRepository ticketRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final PaymentRepository paymentRepository;
    private final SlotAllocationStrategy slotAllocationStrategy;

    public ParkingService(ParkingSlotRepository parkingSlotRepository, VehicleRepository vehicleRepository, TicketRepository ticketRepository, PricingRuleRepository pricingRuleRepository, PaymentRepository paymentRepository, @Qualifier("firstAvailableStrategy") SlotAllocationStrategy slotAllocationStrategy) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.vehicleRepository = vehicleRepository;
        this.ticketRepository = ticketRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.paymentRepository = paymentRepository;
        this.slotAllocationStrategy = slotAllocationStrategy;
    }


    /**
     * Finds the first available slot for a given vehicle type.
     * Note: This is a simple implementation. A better approach would use a Strategy Pattern for extensibility.
     *
     * @param vehicleType The type of vehicle (e.g., CAR, BIKE).
     * @return An Optional containing the available ParkingSlot, or an empty Optional if none found.
     */
    private Optional<ParkingSlot> findAvailableSlot(VehicleType vehicleType) {
        return parkingSlotRepository.findAll().stream()
                .filter(s -> s.getStatus() == SlotStatus.AVAILABLE)
                .filter(s -> isSlotTypeCompatible(s.getSlotType(), vehicleType)) // Ensure slot is large enough
                .findFirst();
    }

    private boolean isSlotTypeCompatible(VehicleType slotType, VehicleType vehicleType) {
        if (vehicleType == VehicleType.BIKE) {
            return true;
        } else if (vehicleType == VehicleType.CAR) {
            return slotType == VehicleType.CAR || slotType == VehicleType.TRUCK;
        } else if (vehicleType == VehicleType.TRUCK) {
            return slotType == VehicleType.TRUCK;
        }
        return false;
    }

    /**
     * Handles the complete parking process for a vehicle.
     * It checks for duplicate entries, finds an available slot,
     * updates the slot status, creates a new vehicle record,
     * and generates a new parking ticket.
     *
     * @param plateNo     The license plate number of the vehicle.
     * @param vehicleType The type of the vehicle.
     * @return The newly created Ticket.
     * @throws IllegalStateException if a slot is unavailable or the vehicle is already parked.
     */
    @Transactional
    public Ticket parkVehicle(String plateNo, VehicleType vehicleType) {

        IVehicle vehicle = IVehicle.createVehicle(vehicleType);

        if (vehicleRepository.findByPlateNo(plateNo).isPresent()) {
            Optional<Ticket> activeTicket = ticketRepository.findByVehiclePlateNoAndStatus(plateNo, TicketStatus.ACTIVE);
            if (activeTicket.isPresent()) {
                throw new IllegalStateException("Vehicle with plate number " + plateNo + " is already parked.");
            }
        }


        Optional<ParkingSlot> optionalSlot = slotAllocationStrategy.findSlot(vehicle);
        if (optionalSlot.isEmpty()) {
            throw new IllegalStateException("Parking lot is full for vehicle type " + vehicleType.name());
        }

        ParkingSlot slot = optionalSlot.get();
        slot.setStatus(SlotStatus.OCCUPIED);
        parkingSlotRepository.save(slot);


        Vehicle car = new Vehicle();
        car.setPlateNo(plateNo);
        car.setVehicleType(vehicleType);
        vehicleRepository.save(car);


        Ticket ticket = new Ticket();
        ticket.setVehiclePlateNo(plateNo);
        ticket.setParkingSlot(slot);
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setStatus(TicketStatus.ACTIVE);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public ReceiptDetails exitVehicle(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket with ID " + ticketId + " not found."));

        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket is not active and cannot be exited.");
        }


        LocalDateTime exitTime = LocalDateTime.now();
        long durationMinutes = ChronoUnit.MINUTES.between(ticket.getEntryTime(), exitTime);
        double durationHours = (double) durationMinutes / 60.0;

        VehicleType vehicleType = ticket.getParkingSlot().getSlotType();
        PricingRule pricingRule = pricingRuleRepository.findByVehicleType(vehicleType)
                .orElseThrow(() -> new IllegalStateException("No pricing rule found for vehicle type " + vehicleType));


        double totalAmount = pricingRule.getBasePrice() + (durationHours * pricingRule.getHourlyRate());


        ticket.setExitTime(exitTime);
        ticket.setStatus(TicketStatus.PAID);
        ticketRepository.save(ticket);

        ParkingSlot slot = ticket.getParkingSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        parkingSlotRepository.save(slot);


        Payment payment = new Payment();
        payment.setTicket(ticket);
        payment.setAmount(totalAmount);
        payment.setPaymentTime(exitTime);
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        paymentRepository.save(payment);


        ReceiptDetails receipt = new ReceiptDetails();
        receipt.setTicketId(ticket.getId());
        receipt.setPlateNo(ticket.getVehiclePlateNo());
        receipt.setEntryTime(ticket.getEntryTime());
        receipt.setExitTime(exitTime);
        receipt.setTotalAmount(Math.round(totalAmount * 100.0) / 100.0);
        receipt.setDurationInMinutes(durationMinutes);

        return receipt;
    }
}