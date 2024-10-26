package com.lockerapp.lockerbackend.controller;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.service.LockerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/lockers")
public class LockerController {

    private final LockerService lockerService;

    public LockerController(LockerService lockerService) {
        this.lockerService = lockerService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<Locker>> getAvailableLockers() {
        List<Locker> availableLockers = lockerService.getAvailableLockers();
        if (availableLockers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Returnăm o listă goală în loc de un wildcard.
        }
        return ResponseEntity.ok(availableLockers);
    }

    @PostMapping("/reserve/{lockerId}")
    public ResponseEntity<?> reserveLocker(@PathVariable Long lockerId) {
        try {
            Locker reservedLocker = lockerService.reserveLocker(lockerId);
            return ResponseEntity.ok(reservedLocker);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    //Acest endpoint va returna detalii despre un locker specific pe baza ID-ului său.
    @GetMapping("/{lockerId}")
    public ResponseEntity<Locker> getLockerById(@PathVariable Long lockerId) {
        Locker locker = lockerService.getLockerById(lockerId);
        if (locker == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(locker);
    }

}
