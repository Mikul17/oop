package org.example;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {
    @Getter
    private static final Cart instance = new Cart();
    private double totalPrice;
    private final Map<Product, Integer> cart = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        if (cart.containsKey(product)) {
            cart.put(product, cart.get(product) + quantity);
        } else {
            cart.put(product, quantity);
        }
        totalPrice += product.getPrice() * quantity;
    }

    public void addProduct(int productId, int quantity) {
        Product product = Catalog.getInstance().getProductById(productId);
        if (product != null) {
            addProduct(product, quantity);
        }
    }

    public void removeProduct(int productId, int quantity) {
        Product product = Catalog.getInstance().getProductById(productId);
        if (cart.containsKey(product)) {
            cart.put(product, cart.get(product) - quantity);
            if (cart.get(product) <= 0) {
                cart.remove(product);
            }
            totalPrice -= product.getPrice() * quantity;
        }
    }

    public void clearCart() {
        cart.clear();
    }
    public int getAmount() {
      int amount = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            amount += entry.getValue();
        }
        return amount;
    }

    public void applyDiscount(double discount) {
        totalPrice = totalPrice - totalPrice * discount;
    }

    public Map<Product, Integer> getCartItems() {
        return cart;
    }
}
