package org.example.smarthomebackend.controller;

import org.example.smarthomebackend.entity.Device;
import org.example.smarthomebackend.service.DeviceService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/devices")
public class DeviceController {

  private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("/{id}/toggle")
    public Mono<Device> toggleDevice(@PathVariable Long id) {
        return deviceService.toggleDevice(id);
    }

    @PutMapping("/{id}/settings")
    public Mono<Device> updateSettings(@PathVariable Long id, @RequestParam String room) {
        return deviceService.updateDeviceSettings(id, room);
    }

    @GetMapping
    public Flux<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }
}
