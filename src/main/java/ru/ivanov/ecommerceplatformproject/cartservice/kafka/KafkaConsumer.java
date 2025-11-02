package ru.ivanov.ecommerceplatformproject.cartservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.ivanov.ecommerceplatformproject.cartservice.service.CartService;
import ru.ivanov.ecommerceplatformproject.sharedlibs.event.ProductUpdatedEvent;
import ru.ivanov.ecommerceplatformproject.sharedlibs.event.UserRegisteredEvent;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CartService cartService;

    @KafkaListener(topics = {"user-registered-event-topic"})
    public void handleUserCreatedEvent(@Payload UserRegisteredEvent event) {
        UUID userId = UUID.fromString(event.userId());
        cartService.createCart(userId);
    }

    @KafkaListener(topics = "product-updated-event-topic")
    public void handleProductUpdatedEvent(@Payload ProductUpdatedEvent event) {
        //todo logic
    }

    @KafkaListener(topics = "product-deleted-event-topic")
    public void test() {} //todo архитектура
}