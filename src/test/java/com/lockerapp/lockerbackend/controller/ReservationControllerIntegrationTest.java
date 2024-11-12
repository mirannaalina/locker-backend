package com.lockerapp.lockerbackend.controller;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.entity.Reservation;
import com.lockerapp.lockerbackend.entity.User;
import com.lockerapp.lockerbackend.repository.ReservationRepository;
import com.lockerapp.lockerbackend.repository.LockerRepository;
import com.lockerapp.lockerbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll(); // Șterge toate rezervările înainte de fiecare test
        lockerRepository.deleteAll(); // Șterge toate locker-urile înainte de fiecare test
        userRepository.deleteAll(); // Șterge toți utilizatorii înainte de fiecare test
    }

    @Test
    void testCreateReservation() throws Exception {
        // Crearea unui locker și a unui utilizator pentru testare
        Locker locker = new Locker("Location A", true);
        lockerRepository.save(locker);

        User user = new User("username", "password"); // Asigură-te că ai un constructor corect
        userRepository.save(user);

        mockMvc.perform(post("/reservations/create/{lockerId}", locker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locker.id").value(locker.getId())); // Verifică că rezervarea este creată
    }

    @Test
    void testGetReservationById_Success() throws Exception {
        // Crează o rezervare pentru testare
        Locker locker = new Locker("Location A", true);
        lockerRepository.save(locker);

        User user = new User("username", "password");
        userRepository.save(user);
        LocalDateTime localDateTime = LocalDateTime.now();

        Reservation reservation = new Reservation(user, locker, localDateTime);
        reservationRepository.save(reservation);

        mockMvc.perform(get("/reservations/{reservationId}", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locker.id").value(locker.getId()))
                .andExpect(jsonPath("$.user.username").value("username"));
    }

    @Test
    void testGetReservationById_NotFound() throws Exception {
        mockMvc.perform(get("/reservations/{reservationId}", 999L)) // ID care nu există
                .andExpect(status().isNotFound());
    }

    @Test
    void testCancelReservation_Success() throws Exception {
        // Crează o rezervare pentru testare
        Locker locker = new Locker("Location A", true);
        lockerRepository.save(locker);
        LocalDateTime localDateTime = LocalDateTime.now();

        User user = new User("username", "password");
        userRepository.save(user);

        Reservation reservation = new Reservation(user, locker, localDateTime);
        reservationRepository.save(reservation);

        mockMvc.perform(delete("/reservations/{reservationId}", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation canceled successfully."));
    }

    @Test
    void testCancelReservation_NotFound() throws Exception {
        mockMvc.perform(delete("/reservations/{reservationId}", 999L)) // ID care nu există
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reservation not found."));
    }
}
