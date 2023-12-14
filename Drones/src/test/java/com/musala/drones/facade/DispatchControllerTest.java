package com.musala.drones.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.facade.dto.DroneDTO;
import com.musala.drones.facade.dto.MedicationDTO;
import com.musala.drones.model.Drone;
import com.musala.drones.model.DroneState;
import com.musala.drones.model.Medication;
import com.musala.drones.service.DroneFactory;
import com.musala.drones.service.MedicationFactory;
import com.musala.drones.service.exception.drone.DroneDuplicateSerialNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DispatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDroneRegister() throws Exception {
        droneRegister(DroneFactory.s123_IDLE_100_500());
    }

    @Test
    public void testSameDroneRegister() throws Exception {
        Drone drone = DroneFactory.s1234_LOADING_100_500();

        //register first drone
        droneRegister(drone);

        //register second drone and except exception
        mockMvc.perform(MockMvcRequestBuilders
                .post("/drone").content(objectMapper.writeValueAsString(DroneDTO.fromDrone(drone)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DroneDuplicateSerialNumberException))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testStartLoading() throws Exception {
        //register drone
        Drone droneInDb = droneRegister(DroneFactory.s12345_IDLE_80_500());

        Set<Long> nazivinAndNurofenIds = getMedicationsIds(getNazivinAndNurofenMedications());

        //load the drone by given medication ids and except status Ok
        MvcResult loadedDroneMvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/drone/start_loading")
                .param("droneId", droneInDb.getId().toString())
                .content(objectMapper.writeValueAsString(nazivinAndNurofenIds))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //check the /drone/start_loading response
        assertResponse(loadedDroneMvcResult.getResponse());

        Drone loadedDrone = objectMapper.readValue(loadedDroneMvcResult.getResponse().getContentAsString(), Drone.class);
        assertNotNull(loadedDrone);
        assertEquals(loadedDrone.getDroneState(), DroneState.LOADING);
    }

    private MvcResult addMediсation(Medication medication) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post("/medication").content(objectMapper.writeValueAsString(MedicationDTO.fromMedication(medication)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    private Drone droneRegister(Drone drone) throws Exception {
        MvcResult droneMvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/drone").content(objectMapper.writeValueAsString(DroneDTO.fromDrone(drone)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        assertResponse(droneMvcResult.getResponse());
        Drone droneInDb = objectMapper.readValue(droneMvcResult.getResponse().getContentAsString(), Drone.class);
        assertNotNull(droneInDb);
        assertNotNull(droneInDb.getId());

        return droneInDb;
    }

    private void assertResponse(MockHttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        Assertions.assertEquals("application/json",
                response.getContentType());
        Assertions.assertFalse(response.getContentAsString().isEmpty());
    }

    private Set<Long> getMedicationsIds(List<Medication> medications) {
        Set<Long> medicationIds = new HashSet<>();
        for (Medication medication : medications) {
            medicationIds.add(medication.getId());
        }
        return medicationIds;
    }

    private List<Medication> getNazivinAndNurofenMedications() throws Exception {
        List<Medication> medications = new ArrayList<>();
        medications.add(addMedication(MedicationFactory.c123_Nazivin_50_i123()));
        medications.add(addMedication(MedicationFactory.c1234_Nurofen_100_i1234()));

        return medications;
    }

    private Medication addMedication(Medication medication) throws Exception {
        MvcResult medicationMvcResult = addMediсation(medication);
        assertResponse(medicationMvcResult.getResponse());
        return objectMapper.readValue(medicationMvcResult.getResponse().getContentAsString(), Medication.class);
    }
}
