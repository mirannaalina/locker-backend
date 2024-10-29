package com.lockerapp.lockerbackend.service;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.repository.LockerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockerServiceTest {

    @Mock
    private LockerRepository lockerRepository;

    @InjectMocks
    private LockerService lockerService;

    @Test
    void testCreateLocker() {
        // Arrange
        Locker newLocker = new Locker("Location A", true);
        Locker savedLocker = new Locker("Location A", true);
        savedLocker.setId(1L); // Simulăm ID-ul generat

        when(lockerRepository.save(any(Locker.class))).thenReturn(savedLocker);

        // Act
        Locker result = lockerService.createLocker(newLocker);

        // Assert
        assertNotNull(result, "Locker-ul creat ar trebui să nu fie null");
        assertEquals(1L, result.getId(), "ID-ul Locker-ului ar trebui să fie 1L după salvare");
        assertEquals("Location A", result.getLocation());
        assertTrue(result.isAvailable());

        // Verificăm dacă lockerRepository.save a fost apelat o singură dată
        verify(lockerRepository, times(1)).save(any(Locker.class));
    }

    @Test
    void testGetAvailableLockers() {
        // Arrange
        Locker locker1 = new Locker("1", true);
        Locker locker2 = new Locker("2", true);
        List<Locker> availableLockers = List.of(locker1, locker2);

        when(lockerRepository.findByIsAvailable(true)).thenReturn(availableLockers);

        // Act
        List<Locker> result = lockerService.getAvailableLockers();

        // Assert
        assertEquals(availableLockers.size(), result.size(), "Numărul de locker-e disponibile ar trebui să fie corect");
        assertEquals(availableLockers, result);
        verify(lockerRepository, times(1)).findByIsAvailable(true);
    }

    @Test
    void testReserveLocker_Success() {
        // Arrange
        Locker locker = new Locker("1", true);
        locker.setId(1L);
        when(lockerRepository.findById(1L)).thenReturn(Optional.of(locker));
        when(lockerRepository.save(any(Locker.class))).thenReturn(locker);

        // Act
        Locker reservedLocker = lockerService.reserveLocker(1L);

        // Assert
        assertFalse(reservedLocker.isAvailable(), "Locker-ul ar trebui să fie rezervat");
        verify(lockerRepository, times(1)).findById(1L);
        verify(lockerRepository, times(1)).save(locker);
    }

    @Test
    void testReserveLocker_LockerNotFound() {
        // Arrange
        when(lockerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> lockerService.reserveLocker(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(lockerRepository, times(1)).findById(1L);
    }

    @Test
    void testReserveLocker_LockerAlreadyReserved() {
        // Arrange
        Locker locker = new Locker("1", false); // Locker este deja rezervat
        when(lockerRepository.findById(1L)).thenReturn(Optional.of(locker));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> lockerService.reserveLocker(1L));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(lockerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLockerById_LockerFound() {
        // Arrange
        Locker locker = new Locker("1", true);
        locker.setId(1L);
        when(lockerRepository.findById(1L)).thenReturn(Optional.of(locker));

        // Act
        Locker result = lockerService.getLockerById(1L);

        // Assert
        assertNotNull(result, "Locker-ul ar trebui să nu fie null");
        assertEquals(locker, result);
        verify(lockerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLockerById_LockerNotFound() {
        // Arrange
        when(lockerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Locker result = lockerService.getLockerById(1L);

        // Assert
        assertNull(result, "Locker-ul ar trebui să fie null");
        verify(lockerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllLockers() {
        // Arrange
        Locker locker1 = new Locker("1", true);
        Locker locker2 = new Locker("2", false);
        List<Locker> allLockers = List.of(locker1, locker2);

        when(lockerRepository.findAll()).thenReturn(allLockers);

        // Act
        List<Locker> result = lockerService.getAllLockers();

        // Assert
        assertEquals(allLockers.size(), result.size(), "Numărul total de locker-e ar trebui să fie corect");
        assertEquals(allLockers, result);
        verify(lockerRepository, times(1)).findAll();
    }
}
