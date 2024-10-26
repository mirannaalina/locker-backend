package com.lockerapp.lockerbackend.controller;

import com.lockerapp.lockerbackend.entity.Reservation;
import com.lockerapp.lockerbackend.entity.User;
import com.lockerapp.lockerbackend.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create/{lockerId}")
    public Reservation createReservation(@PathVariable Long lockerId, @RequestBody User user) {
        return reservationService.createReservation(user, lockerId);
    }

    //Acest endpoint va returna detalii despre o rezervare existentă.
    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(reservation);
    }

    //Acest endpoint va permite anularea unei rezervări pe baza ID-ului său.
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long reservationId) {
        boolean success = reservationService.cancelReservation(reservationId);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found.");
        }
        return ResponseEntity.ok("Reservation canceled successfully.");
    }
}
