package org.example.smarthomebackend.service;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class DeviceWebSocketHandler implements WebSocketHandler {

    private final DeviceService deviceService;

    public DeviceWebSocketHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    @NonNull
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                session.receive()
                        .map(msg -> Long.valueOf(msg.getPayloadAsText()))
                        .flatMap(deviceService::toggleDevice)
                        .map(device -> session.textMessage("Device state updated: " + device))
        );
    }
}
