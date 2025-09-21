package com.walkingtree.parkinglot.controller;

import com.walkingtree.parkinglot.dto.ExitVehicleRequest;
import com.walkingtree.parkinglot.dto.ParkVehicleRequest;
import com.walkingtree.parkinglot.dto.ReceiptDetails;
import com.walkingtree.parkinglot.model.Ticket;
import com.walkingtree.parkinglot.service.ParkingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final ParkingService parkingService;

    public UserController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * Handles the vehicle parking request from a user.
     * It assigns a slot and generates a new ticket.
     *
     * @param request The request body containing vehicle details.
     * @return The created Ticket object.
     */
    @PostMapping("/park")
    public ResponseEntity<Ticket> parkVehicle(@RequestBody ParkVehicleRequest request) {
        Ticket ticket = parkingService.parkVehicle(request.getPlateNo(), request.getVehicleType());
        return ResponseEntity.ok(ticket);
    }

    /**
     * Handles the vehicle exit request. It calculates the fee and generates a receipt.
     *
     * @param request The request body containing the ticket ID.
     * @return A ReceiptDetails object with the final cost and parking information.
     */
    @PostMapping("/exit")
    public ResponseEntity<ReceiptDetails> exitVehicle(@RequestBody ExitVehicleRequest request) {
        ReceiptDetails receipt = parkingService.exitVehicle(request.getTicketId());
        return ResponseEntity.ok(receipt);
    }
}