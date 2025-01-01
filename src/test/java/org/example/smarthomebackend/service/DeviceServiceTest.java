package org.example.smarthomebackend.service;

import org.example.smarthomebackend.entity.Device;
import org.example.smarthomebackend.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toggleDevice() {
        // Arrange
        Long deviceId = 1L;

        Device device = new Device();
        device.setId(deviceId);
        device.setName("Smart Light");
        device.setType("light");
        device.setStatus(false);
        device.setLastUpdated(LocalDateTime.now());

        Device updatedDevice = new Device();
        updatedDevice.setId(deviceId);
        updatedDevice.setName("Smart Light");
        updatedDevice.setType("light");
        updatedDevice.setStatus(true);
        updatedDevice.setLastUpdated(LocalDateTime.now());

        when(deviceRepository.findById(deviceId)).thenReturn(Mono.just(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(Mono.just(updatedDevice));

        // Act
        Mono<Device> result = deviceService.toggleDevice(deviceId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(d -> d.getId().equals(deviceId) && d.isStatus())
                .verifyComplete();

        verify(deviceRepository, times(1)).findById(deviceId);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void updateDeviceSettings() {
        // Arrange
        Long deviceId = 1L;
        String room = "test";

        Device device = new Device();
        device.setId(deviceId);
        device.setName("Smart Light");
        device.setType("light");
        device.setStatus(false);
        device.setLastUpdated(LocalDateTime.now());

        Device updatedDevice = new Device();
        updatedDevice.setId(deviceId);
        updatedDevice.setName("Smart Light");
        updatedDevice.setType("light");
        updatedDevice.setStatus(true);
        updatedDevice.setLastUpdated(LocalDateTime.now());

        when(deviceRepository.findById(deviceId)).thenReturn(Mono.just(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(Mono.just(updatedDevice));

        // Act
        Mono<Device> result = deviceService.updateDeviceSettings(deviceId, room);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(d -> d.getId().equals(deviceId))
                .verifyComplete();

        verify(deviceRepository, times(1)).findById(deviceId);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void getAllDevices() {
        // Arrange
        Device device = new Device();
        device.setId(1L);
        device.setName("Smart Light");
        device.setType("light");
        device.setStatus(false);
        device.setLastUpdated(LocalDateTime.now());

        when(deviceRepository.findAll()).thenReturn(Flux.just(device));

        // Act
        Device result = deviceService.getAllDevices().blockFirst();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Smart Light", result.getName());

        verify(deviceRepository, times(1)).findAll();
    }
}
