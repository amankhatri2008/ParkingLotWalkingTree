package com.walkingtree.parkinglot.repository;

import com.walkingtree.parkinglot.enums.TicketStatus;
import com.walkingtree.parkinglot.model.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByVehiclePlateNoAndStatus_shouldReturnEmpty_whenVehicleIsInactive() {
        Optional<Ticket> foundTicket = ticketRepository.findByVehiclePlateNoAndStatus("XYZ-987", TicketStatus.ACTIVE);
        assertFalse(foundTicket.isPresent());
    }

    @Test
    void findByVehiclePlateNoAndStatus_shouldReturnEmpty_whenVehiclePlateNotFound() {
        Optional<Ticket> foundTicket = ticketRepository.findByVehiclePlateNoAndStatus("NON-EXISTENT", TicketStatus.ACTIVE);
        assertFalse(foundTicket.isPresent());
    }

    @Test
    void findAllByStatus_shouldNotReturnActiveTickets() {
        List<Ticket> activeTickets = ticketRepository.findAllByStatus(TicketStatus.ACTIVE);
        assertTrue(activeTickets.isEmpty());
        assertEquals(0, activeTickets.size());
    }

}