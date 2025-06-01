package org.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.example.TestUtils.sampleProductWithId;
import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @BeforeEach
    public void setUp() {
        Catalog.getInstance().getProductList().clear();
        Cart.getInstance().getCartItems().clear();
    }

    @Test
    void shouldAddToCart() {
        //given
        var product = sampleProductWithId(1);
        Catalog.getInstance().getProductList().add(product);
        //when
        Cart.getInstance().addProduct(1, 3);;
        //then
        assertTrue(Cart.getInstance().getCartItems().containsKey(product));
        assertEquals(3, Cart.getInstance().getCartItems().get(product));
        assertEquals(3, Cart.getInstance().getAmountOfProducts());
        assertEquals(1, Cart.getInstance().getCartItems().size());
    }

    @Test
    void shouldNotAddUnavailableProduct() {
        //given
        var cart = Cart.getInstance();
        //when
        cart.addProduct(-123, 6);
        //then
        assertTrue(Cart.getInstance().getCartItems().isEmpty());
    }

    @Test
    void shouldRemoveFromCart() {
        //given
        var product = sampleProductWithId(1);
        Catalog.getInstance().getProductList().add(product);
        var cart = Cart.getInstance();

        //when
        cart.addProduct(1, 1);
        cart.removeProduct(1, 1);

        //then
        assertFalse(cart.getCartItems().containsKey(product));
    }

    @Test
    void shouldNotRemoveProductNotInCart() {
        //given
        var product = sampleProductWithId(1);
        Catalog.getInstance().getProductList().add(product);
        var cart = Cart.getInstance();
        cart.addProduct(1, 1);

        //when
        cart.removeProduct(2, 1);

        //then
        assertFalse(cart.getCartItems().isEmpty());
        assertTrue(cart.getCartItems().containsKey(product));
    }

    @Test
    void shouldNotApplyDiscountFromUnknowCode(){
        //given
        var cart = Cart.getInstance();

        //when
        cart.applyDiscount("NOT_EXISTING_PROMOTION");

        //then
        assertEquals(cart.getActivePromotionCode(), Optional.empty());
    }
}
