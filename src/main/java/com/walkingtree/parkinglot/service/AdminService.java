package com.walkingtree.parkinglot.service;

import com.walkingtree.parkinglot.dto.ParkedVehicleInfo;
import com.walkingtree.parkinglot.dto.ParkingStatusResponse;
import com.walkingtree.parkinglot.enums.SlotStatus;
import com.walkingtree.parkinglot.enums.TicketStatus;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.model.PricingRule;
import com.walkingtree.parkinglot.model.Ticket;
import com.walkingtree.parkinglot.repository.ParkingSlotRepository;
import com.walkingtree.parkinglot.repository.PricingRuleRepository;
import com.walkingtree.parkinglot.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final PricingRuleRepository pricingRuleRepository;
    private final TicketRepository ticketRepository;

    public AdminService(ParkingSlotRepository parkingSlotRepository, PricingRuleRepository pricingRuleRepository, TicketRepository ticketRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.pricingRuleRepository = pricingRuleRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Adds a new parking slot to the system.
     * The new slot is initially set to AVAILABLE status.
     *
     * @param slot The ParkingSlot object to be added.
     * @return The saved ParkingSlot object.
     */
    @Transactional
    public ParkingSlot addSlot(ParkingSlot slot) {
        slot.setStatus(SlotStatus.AVAILABLE);
        return parkingSlotRepository.save(slot);
    }

    /**
     * Removes a parking slot by its ID.
     * Throws an exception if the slot is occupied.
     *
     * @param id The ID of the slot to be removed.
     */
    @Transactional
    public void removeSlot(Long id) {
        Optional<ParkingSlot> optionalSlot = parkingSlotRepository.findById(id);
        if (optionalSlot.isPresent()) {
            ParkingSlot slot = optionalSlot.get();
            if (slot.getStatus() == SlotStatus.OCCUPIED) {
                throw new IllegalStateException("Cannot remove an occupied slot.");
            }
            parkingSlotRepository.delete(slot);
        } else {
            throw new IllegalArgumentException("Parking slot with ID " + id + " not found.");
        }
    }

    /**
     * Adds or updates a pricing rule for a specific vehicle type.
     *
     * @param rule The PricingRule object to be added or updated.
     * @return The saved PricingRule object.
     */
    @Transactional
    public PricingRule addPricingRule(PricingRule rule) {

        Optional<PricingRule> existingRule = pricingRuleRepository.findByVehicleType(rule.getVehicleType());
        if (existingRule.isPresent()) {
            PricingRule updatedRule = existingRule.get();
            updatedRule.setBasePrice(rule.getBasePrice());
            updatedRule.setHourlyRate(rule.getHourlyRate());
            return pricingRuleRepository.save(updatedRule);
        } else {
            return pricingRuleRepository.save(rule);
        }
    }

    @Transactional(readOnly = true)
    public ParkingStatusResponse getParkingStatus() {
        long totalSlots = parkingSlotRepository.count();
        long availableSlots = parkingSlotRepository.countByStatus(SlotStatus.AVAILABLE);
        long occupiedSlots = totalSlots - availableSlots;

        List<Ticket> activeTickets = ticketRepository.findAllByStatus(TicketStatus.ACTIVE);

        List<ParkedVehicleInfo> parkedVehicles = activeTickets.stream()
                .map(ticket -> {
                    ParkedVehicleInfo info = new ParkedVehicleInfo();
                    info.setTicketId(ticket.getId());
                    info.setPlateNo(ticket.getVehiclePlateNo());
                    info.setSlotId(ticket.getParkingSlot().getId());
                    info.setEntryTime(ticket.getEntryTime());
                    return info;
                })
                .collect(Collectors.toList());

        ParkingStatusResponse response = new ParkingStatusResponse();
        response.setTotalSlots(totalSlots);
        response.setAvailableSlots(availableSlots);
        response.setOccupiedSlots(occupiedSlots);
        response.setParkedVehicles(parkedVehicles);

        return response;
    }
}