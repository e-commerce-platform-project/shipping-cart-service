package ru.ivanov.ecommerceplatformproject.cartservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "carts")
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart(UUID userId) {
        this.userId = userId;
    }

    public void changeItemQuantity(UUID productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.updateQuantity(quantity),
                        () -> {
                            int totalQuantity = getCartTotalQuantity();
                            if (totalQuantity < 100 && totalQuantity + quantity <= 100) {
                                cartItems.add(new CartItem(this, productId, quantity));
                            } else {
                                throw new IllegalArgumentException("Cart max amount must be 100 or less");//todo
                            }
                        }
                );
    }

    private int getCartTotalQuantity() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public void deleteItem(UUID productId) {
        cartItems.removeIf(item -> item.getProductId().equals(productId));
    }

    public void clear() {
        cartItems.clear();
    }
}