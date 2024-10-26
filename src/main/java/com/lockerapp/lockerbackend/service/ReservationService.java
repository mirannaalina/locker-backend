package com.lockerapp.lockerbackend.service;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.entity.Reservation;
import com.lockerapp.lockerbackend.entity.User;
import com.lockerapp.lockerbackend.repository.ReservationRepository;
import com.lockerapp.lockerbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationService {

    private final LockerService lockerService;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);


    public ReservationService(LockerService lockerService, ReservationRepository reservationRepository, UserRepository userRepository) {
        this.lockerService = lockerService;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public Reservation createReservation(User user, Long lockerId) {
        // Salvăm utilizatorul dacă nu există deja în baza de date
        logger.info("Trying to reserve locker with ID: {}", lockerId);
        logger.info("User details: Username - {}, Password - {}", user.getUsername(), user.getPassword());

        User savedUser = userRepository.findByUsername(user.getUsername())
                .orElseGet(() -> {
                    logger.info("User not found, creating new user");
                    return userRepository.save(user);
                });

        Locker locker = lockerService.reserveLocker(lockerId);
        logger.info("Locker reserved: ID - {}, Location - {}", locker.getId(), locker.getLocation());
        Reservation reservation = new Reservation(savedUser, locker, LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    public boolean cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            reservationRepository.delete(reservation.get());
            return true;
        }
        return false;
    }
}