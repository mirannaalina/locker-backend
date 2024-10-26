package com.lockerapp.lockerbackend.repository;

import com.lockerapp.lockerbackend.entity.Reservation;
import com.lockerapp.lockerbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
}