package com.lockerapp.lockerbackend.repository;

import com.lockerapp.lockerbackend.entity.Locker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    List<Locker> findByIsAvailable(boolean isAvailable);
}