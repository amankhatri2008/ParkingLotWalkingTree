package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.enums.TicketStatus;
import com.walkingtree.parkinglot.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Finds an active ticket for a given vehicle plate number.
     * This is used to check if a vehicle is currently parked.
     *
     * @param vehiclePlateNo The license plate number of the vehicle.
     * @param status         The status of the ticket (e.g., Ticket.TicketStatus.ACTIVE).
     * @return An Optional containing the active Ticket if found, otherwise empty.
     */
    Optional<Ticket> findByVehiclePlateNoAndStatus(String vehiclePlateNo, TicketStatus status);

    List<Ticket> findAllByStatus(TicketStatus status);
}