package ru.ivanov.ecommerceplatformproject.cartservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;
import ru.ivanov.ecommerceplatformproject.sharedlibs.event.UserRegisteredEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final CartService cartService;

    @KafkaListener(topics = "user-registered-event-topic")
    public void handleUserCreatedEvent(@Payload UserRegisteredEvent event) {
        cartService.createCart(UUID.fromString(event.userId()));
    }
}