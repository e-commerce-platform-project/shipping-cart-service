package ru.ivanov.ecommerceplatformproject.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.ecommerceplatformproject.cartservice.model.Cart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findCartByUserId(UUID userId);

    void deleteByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}