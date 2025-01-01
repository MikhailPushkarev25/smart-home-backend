package org.example.smarthomebackend.repository;

import org.example.smarthomebackend.entity.Device;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DeviceRepository extends ReactiveCrudRepository<Device, Long> {
}
