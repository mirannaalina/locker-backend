package com.lockerapp.lockerbackend.service;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.repository.LockerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LockerService {

    private final LockerRepository lockerRepository;

    public LockerService(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    public List<Locker> getAvailableLockers() {
        return lockerRepository.findByIsAvailable(true);
    }

    public Locker reserveLocker(Long lockerId) {
        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Locker not found"));

        if (!locker.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Locker is already reserved");
        }

        locker.setAvailable(false);
        return lockerRepository.save(locker);
    }

    public Locker getLockerById(Long lockerId) {
        return lockerRepository.findById(lockerId).orElse(null);
    }

    public Locker createLocker(Locker locker) {
        return lockerRepository.save(locker);
    }

    public List<Locker> getAllLockers() {
        return lockerRepository.findAll();
    }
}
