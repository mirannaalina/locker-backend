package com.lockerapp.lockerbackend.controller;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.repository.LockerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class LockerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LockerRepository lockerRepository;

    @BeforeEach
    void setUp() {
        lockerRepository.deleteAll(); // Șterge toate înregistrările înainte de fiecare test
    }

    @Test
    void testCreateLocker() throws Exception {
        mockMvc.perform(post("/lockers/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"location\":\"Location A\", \"isAvailable\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.available").value(false)); // Verifică că locker-ul nu este disponibil
    }

    @Test
    void testGetAvailableLockers() throws Exception {
        Locker locker1 = new Locker("Location A", true);
        Locker locker2 = new Locker("Location B", true);
        lockerRepository.save(locker1);
        lockerRepository.save(locker2);

        mockMvc.perform(get("/lockers/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value("Location A"))
                .andExpect(jsonPath("$[1].location").value("Location B"));
    }

    @Test
    void testReserveLocker_Success() throws Exception {
        Locker locker = new Locker("Test Location", true);
        lockerRepository.save(locker); // Creează un locker pentru testare
        long lockerId = locker.getId();

        mockMvc.perform(post("/lockers/reserve/{lockerId}", lockerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Test Location"))
                .andExpect(jsonPath("$.available").value(false)); // Verifică că locker-ul nu este disponibil
    }

    @Test
    void testGetLockerById_Success() throws Exception {
        Locker locker = new Locker("Location A", true);
        lockerRepository.save(locker); // Creează un locker pentru testare
        long lockerId = locker.getId();

        mockMvc.perform(get("/lockers/{lockerId}", lockerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Location A"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testGetLockerById_NotFound() throws Exception {
        mockMvc.perform(get("/lockers/{lockerId}", 999L)) // ID care nu există
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllLockers() throws Exception {
        Locker locker1 = new Locker("Location A", true);
        Locker locker2 = new Locker("Location B", false);
        lockerRepository.save(locker1);
        lockerRepository.save(locker2);

        mockMvc.perform(get("/lockers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value("Location A"))
                .andExpect(jsonPath("$[1].location").value("Location B"));
    }
}
