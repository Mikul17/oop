package org.example;

import lombok.*;
import org.example.promotion.PromotionCode;
import org.example.product.Product;
import org.example.product.PromotionProduct;
import org.example.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {
    @Getter
    private static final Cart instance = new Cart();
    @Getter
    private double totalPrice;
    private Map<Product, Integer> cart = new HashMap<>();
    private PromotionCode promotionCode;

    public void addProduct(int productId, int quantity) {
        Product product = Catalog.getInstance().getProductById(productId);
        if (product != null) {
            insertOrUpdate(product, quantity);
            product.updateStock(-quantity);
            recalculateTotalPrice();
            System.out.println(quantity + " x " + product.getName() + " added to cart. (Press Enter to continue)");
        }
    }

    public void removeProduct(int productId, int quantity) {
        if (promotionCode != null) {
            System.out.println("Removing applied promotion code");
            this.promotionCode = null;
        }
        Product product = Catalog.getInstance().getProductById(productId);
        if (product != null) {
            insertOrUpdate(product, -quantity);
            product.updateStock(quantity);
            recalculateTotalPrice();
            System.out.println("Removed " + quantity + " product(s) with Id " + productId + " (Press Enter to continue)");
        }
    }

    public void clearCart() {
        restoreMap();
        for (Product product : cart.keySet()) {
            var quantity = cart.get(product);
            Catalog.getInstance().getProductById(product.getId()).updateStock(quantity);
        }
        cart.clear();
        promotionCode = null;
        recalculateTotalPrice();
    }

    public int getAmountOfProducts() {
        int amount = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            amount += entry.getValue();
        }
        return amount;
    }

    public void applyDiscount(String code) {
        PromotionCode promotionCode = PromotionCode.from(code).orElse(null);
        if (promotionCode != null) {
            this.promotionCode = promotionCode;
            restoreMap();
            System.out.println("\nDiscount applied - " + promotionCode.getDescription() + " (Press Enter to continue)");
            handlePromotionChange();
            return;
        }
        System.out.println("Invalid promotion code. (Press Enter to continue)");
    }

    public Map<Product, Integer> getCartItems() {
        return cart;
    }

    public Optional<Integer> getCartQuantity(int productId) {
        Product product = Catalog.getInstance().getProductById(productId);
        return cart.containsKey(product) ? Optional.of(cart.get(product)) : Optional.empty();
    }

    public void handlePromotionChange() {
        if (promotionCode == null) {
            return;
        }
        restoreMap();
        this.cart = this.promotionCode.apply(cart);
        recalculateTotalPrice();
    }

    public void restoreMap() {
        this.cart = MapUtils.mapInheritedToBase(
                cart,
                key -> (key instanceof PromotionProduct p) ?
                        new Product(p.getId(), p.getName(), p.getOriginalPrice(), p.getCategory(), p.getStock(), p.isAvailable())
                        : key
                ,
                Integer::sum
        );
    }

    public Optional<String> getActivePromotionCode() {
        if (this.promotionCode == null) {
            return Optional.empty();
        }
        return Optional.of(this.promotionCode.getPromotionCode());
    }

    private void insertOrUpdate(Product product, int quantity) {
        if (cart.containsKey(product)) {
            cart.put(product, cart.get(product) + quantity);
        } else {
            cart.put(product, quantity);
        }
        if (cart.get(product) <= 0) {
            cart.remove(product);
        }
    }

    private void recalculateTotalPrice() {
        totalPrice = 0;

        if (cart.isEmpty()) {
            return;
        }

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
    }
}
