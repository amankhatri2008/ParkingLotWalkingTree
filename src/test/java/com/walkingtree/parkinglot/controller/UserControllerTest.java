package com.walkingtree.parkinglot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkingtree.parkinglot.dto.ExitVehicleRequest;
import com.walkingtree.parkinglot.dto.ParkVehicleRequest;
import com.walkingtree.parkinglot.dto.ReceiptDetails;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.Ticket;
import com.walkingtree.parkinglot.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void parkVehicle_shouldReturnCreatedTicket_whenSuccessful() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setId(101L);
        ticket.setVehiclePlateNo("ABC-123");

        ParkVehicleRequest request = new ParkVehicleRequest("ABC-123", VehicleType.CAR);

        when(parkingService.parkVehicle(anyString(), any(VehicleType.class))).thenReturn(ticket);


        mockMvc.perform(post("/user/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L));
    }


    @Test
    void exitVehicle_shouldReturnReceiptDetails_whenSuccessful() throws Exception {

        ReceiptDetails receiptDetails = new ReceiptDetails();
        receiptDetails.setTicketId(101L);
        receiptDetails.setTotalAmount(15.0);

        ExitVehicleRequest request = new ExitVehicleRequest(101L);

        when(parkingService.exitVehicle(anyLong())).thenReturn(receiptDetails);


        mockMvc.perform(post("/user/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(101L))
                .andExpect(jsonPath("$.totalAmount").value(15.0));
    }
}