package org.example;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {
    @Getter
    private static final Cart instance = new Cart();
    @Getter
    private double totalPrice;
    private final Map<Product, Integer> cart = new HashMap<>();

    public void addProduct(int productId, int quantity) {
        Product product = Catalog.getInstance().getProductById(productId);
        if (product != null) {
            insertOrUpdate(product, quantity);
            product.updateStock(-quantity);
            totalPrice += product.getPrice() * quantity;
            System.out.println(quantity + " x "+ product.getName() +" added to cart. (Press Enter to continue)");
        }
    }

    public void removeProduct(int productId, int quantity) {
        Product product = Catalog.getInstance().getProductById(productId);
        if (product != null) {
            insertOrUpdate(product, -quantity);
            product.updateStock(quantity);
            totalPrice -= product.getPrice() * quantity;
        }
    }

    public void clearCart() {
        for(Product product : cart.keySet()) {
            product.updateStock(cart.get(product));
        }
        cart.clear();
        totalPrice = 0;
    }

    public int getAmountOfProducts() {
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

    public Optional<Integer> getCardQuantity(int productId) {
        Product product = Catalog.getInstance().getProductById(productId);
        return cart.containsKey(product) ? Optional.of(cart.get(product)) : Optional.empty();
    }

    private void insertOrUpdate(Product product, int quantity) {
        if (cart.containsKey(product)) {
            cart.put(product, cart.get(product) + quantity);
        } else {
            cart.put(product, quantity);
        }
        if(cart.get(product) <= 0) {
            cart.remove(product);
        }
    }
}
