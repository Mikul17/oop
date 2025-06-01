package org.example;

import org.example.promotion.PromotionCode;
import org.example.product.PromotionProduct;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.example.utils.TestUtils.sampleProduct;
import static org.example.utils.TestUtils.sampleProductWithId;
import static org.junit.jupiter.api.Assertions.*;


class PromotionTest {

    @Test
    void shouldApplyTenPercentDiscount() {
        //given
        var product = sampleProduct();
        var discountedProduct = new PromotionProduct(product, 9);
        var map = new HashMap<>(Map.of(product, 1));

        //when
        var result = PromotionCode.TEN_PERCENT.apply(map);

        //then
        assertTrue(result.containsKey(discountedProduct));
    }

    @Test
    void shouldApplyOneForHalfPromotionCode() {
        //given
        var product1 = sampleProduct();
        var discountedProduct = new PromotionProduct(product1, 5);
        var map = new HashMap<>(Map.of(product1, 2));

        //when
        var result = PromotionCode.ONE_FOR_HALF.apply(map);

        //then
        assertEquals(1, map.size());
        assertEquals(2, result.size());
        assertTrue(result.containsKey(discountedProduct));
    }

    @Test
    void shouldNotApplyOneForHalfPromotionCodeForDifferentProducts() {
        //given
        var product1 = sampleProductWithId(1);
        var product2 = sampleProductWithId(2);
        var discountedProduct = new PromotionProduct(product1, 5);
        var discountedProduct2 = new PromotionProduct(product2, 5);
        var map = new HashMap<>(Map.of(product1, 1, product2, 1));

        //when
        var result = PromotionCode.ONE_FOR_HALF.apply(map);

        assertEquals(2, map.size());
        assertEquals(2, result.size());
        assertFalse(result.containsKey(discountedProduct));
        assertFalse(result.containsKey(discountedProduct2));
    }

    @Test
    void shouldApplyCheapestForOnePromotionCode() {
        //given
        var product1 = sampleProduct();
        var discountedProduct = new PromotionProduct(product1, 1);
        var map = new HashMap<>(Map.of(product1, 3));

        //when
        var result = PromotionCode.CHEAPEST_FOR_ONE.apply(map);

        //then
        assertEquals(1, map.size());
        assertEquals(2, result.size());
        assertTrue(result.containsKey(discountedProduct));
    }

    @Test
    void shouldCancelPromotionAfterRemovingItems() {
        //given
        var product = sampleProduct();
        Catalog.getInstance().getProductList().add(product);
        var cart = Cart.getInstance();
        cart.addProduct(1, 3);

        //when
        cart.applyDiscount(PromotionCode.CHEAPEST_FOR_ONE.getPromotionCode());
        cart.removeProduct(1, 1);

        //then
        assertEquals(cart.getActivePromotionCode(), Optional.empty());
    }

    @Test
    void shouldNotApplySamePromotionCodeTwice() {
        //given
        var product = sampleProduct();
        var promotionProduct = new PromotionProduct(product, 9);
        Catalog.getInstance().getProductList().add(product);
        var cart = Cart.getInstance();
        cart.addProduct(1, 1);

        //when
        cart.applyDiscount(PromotionCode.TEN_PERCENT.getPromotionCode());
        cart.applyDiscount(PromotionCode.TEN_PERCENT.getPromotionCode());

        //then
        assertEquals(cart.getActivePromotionCode(), Optional.of(PromotionCode.TEN_PERCENT.getPromotionCode()));
        assertTrue(cart.getCartItems().containsKey(promotionProduct));
    }

}