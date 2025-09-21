package com.walkingtree.parkinglot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkingtree.parkinglot.dto.ParkingStatusResponse;
import com.walkingtree.parkinglot.enums.VehicleType;
import com.walkingtree.parkinglot.model.ParkingSlot;
import com.walkingtree.parkinglot.model.PricingRule;
import com.walkingtree.parkinglot.service.AdminService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }


    @Test
    void addSlot_shouldReturnCreatedSlot_whenSuccessful() throws Exception {

        ParkingSlot newSlot = new ParkingSlot();
        newSlot.setId(1L);
        newSlot.setSlotType(VehicleType.CAR);
        newSlot.setFloor(1L);

        when(adminService.addSlot(any(ParkingSlot.class))).thenReturn(newSlot);


        mockMvc.perform(post("/admin/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSlot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slotType").value("CAR"));
    }


    @Test
    void removeSlot_shouldReturnNoContent_whenSuccessful() throws Exception {

        doNothing().when(adminService).removeSlot(1L);


        mockMvc.perform(delete("/admin/slots/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void addPricingRule_shouldReturnCreatedRule_whenSuccessful() throws Exception {

        PricingRule newRule = new PricingRule();
        newRule.setId(1L);
        newRule.setVehicleType(VehicleType.CAR);
        newRule.setBasePrice(10.0);

        when(adminService.addPricingRule(any(PricingRule.class))).thenReturn(newRule);


        mockMvc.perform(post("/admin/pricing-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleType").value("CAR"))
                .andExpect(jsonPath("$.basePrice").value(10.0));
    }


    @Test
    void getParkingStatus_shouldReturnStatusResponse_whenSuccessful() throws Exception {

        ParkingStatusResponse response = new ParkingStatusResponse();
        response.setTotalSlots(10L);
        response.setAvailableSlots(7L);
        response.setOccupiedSlots(3L);

        when(adminService.getParkingStatus()).thenReturn(response);


        mockMvc.perform(get("/admin/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSlots").value(10L))
                .andExpect(jsonPath("$.availableSlots").value(7L))
                .andExpect(jsonPath("$.occupiedSlots").value(3L));
    }
}