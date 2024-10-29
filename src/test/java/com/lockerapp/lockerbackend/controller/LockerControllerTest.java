package com.lockerapp.lockerbackend.controller;

import com.lockerapp.lockerbackend.entity.Locker;
import com.lockerapp.lockerbackend.service.LockerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LockerControllerTest {

    @Mock
    private LockerService lockerService;

    @InjectMocks
    private LockerController lockerController;

    private Locker locker;

    @BeforeEach
    void setUp() {
        locker = new Locker("1", true); // Exemplu de locker
    }

    @Test
    void testGetAvailableLockers() {
        List<Locker> availableLockers = Collections.singletonList(locker);
        when(lockerService.getAvailableLockers()).thenReturn(availableLockers);

        ResponseEntity<List<Locker>> response = lockerController.getAvailableLockers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(availableLockers, response.getBody());
        verify(lockerService, times(1)).getAvailableLockers();
    }

    @Test
    void testGetAvailableLockers_Empty() {
        when(lockerService.getAvailableLockers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Locker>> response = lockerController.getAvailableLockers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(lockerService, times(1)).getAvailableLockers();
    }

    @Test
    void testReserveLocker_Success() {
        when(lockerService.reserveLocker(1L)).thenReturn(locker);

        ResponseEntity<?> response = lockerController.reserveLocker(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locker, response.getBody());
        verify(lockerService, times(1)).reserveLocker(1L);
    }

    @Test
    void testReserveLocker_NotFound() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(lockerService).reserveLocker(1L);

        ResponseEntity<?> response = lockerController.reserveLocker(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(lockerService, times(1)).reserveLocker(1L);
    }

    @Test
    void testGetLockerById_Found() {
        when(lockerService.getLockerById(1L)).thenReturn(locker);

        ResponseEntity<Locker> response = lockerController.getLockerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locker, response.getBody());
        verify(lockerService, times(1)).getLockerById(1L);
    }

    @Test
    void testGetLockerById_NotFound() {
        when(lockerService.getLockerById(1L)).thenReturn(null);

        ResponseEntity<Locker> response = lockerController.getLockerById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(lockerService, times(1)).getLockerById(1L);
    }

    @Test
    void testCreateLocker() {
        when(lockerService.createLocker(locker)).thenReturn(locker);

        ResponseEntity<Locker> response = lockerController.createLocker(locker);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(locker, response.getBody());
        verify(lockerService, times(1)).createLocker(locker);
    }

    @Test
    void testGetAllLockers() {
        List<Locker> lockers = Collections.singletonList(locker);
        when(lockerService.getAllLockers()).thenReturn(lockers);

        ResponseEntity<List<Locker>> response = lockerController.getAllLockers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lockers, response.getBody());
        verify(lockerService, times(1)).getAllLockers();
    }
}
