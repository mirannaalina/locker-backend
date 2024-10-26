package com.lockerapp.lockerbackend;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.repository.LockerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final LockerRepository lockerRepository;

    public DataLoader(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Adaugă câteva lockere inițiale în baza de date
        lockerRepository.save(new Locker("Location A", true));
        lockerRepository.save(new Locker("Location B", true));
        lockerRepository.save(new Locker("Location C", false));
        lockerRepository.save(new Locker("Location D", true));
        lockerRepository.save(new Locker("Location E", true));


    }
}