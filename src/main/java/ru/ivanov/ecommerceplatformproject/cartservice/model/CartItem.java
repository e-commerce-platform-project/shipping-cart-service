package ru.ivanov.ecommerceplatformproject.cartservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ivanov.ecommerceplatformproject.cartservice.util.CartItemQuantityConverter;
import ru.ivanov.ecommerceplatformproject.cartservice.model.valueobjects.CartItemQuantity;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    @Convert(converter = CartItemQuantityConverter.class)
    private CartItemQuantity quantity;

    public CartItem(Cart cart, UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = new CartItemQuantity(quantity);
        this.cart = cart;
    }

    public void updateQuantity(int quantity) {
        this.quantity = new CartItemQuantity(quantity);
    }

    public int getQuantity() {
        return this.quantity.quantity();
    }
}