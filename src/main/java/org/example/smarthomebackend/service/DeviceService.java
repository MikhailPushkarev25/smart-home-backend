package org.example.smarthomebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smarthomebackend.entity.Device;
import org.example.smarthomebackend.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Mono<Device> toggleDevice(Long id) {
        return deviceRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Device with ID " + id + " not found")))
                .flatMap(device -> {
                    device.setStatus(!device.isStatus());
                    device.setLastUpdated(LocalDateTime.now());
                    return deviceRepository.save(device);
                });
    }

    public Mono<Device> updateDeviceSettings(Long id, String room) {
        return deviceRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Device with ID " + id + " not found")))
                .flatMap(device -> {
                    device.setRoom(room);
                    device.setLastUpdated(LocalDateTime.now());
                    return deviceRepository.save(device);
                });
    }

    public Flux<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
