package org.example;

import org.example.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.example.TestUtils.initializeCatalogWithSampleProduct;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @BeforeEach
    public void setUp() {
        Catalog.getInstance().getProductList().clear();
    }

    @Test
    public void shouldAddWhenValidQty(){
        //given
        initializeCatalogWithSampleProduct();
        //when
        var expected = ProductService.getInstance().validateAdditionQuantity("1", 1);
        //then
        assertTrue(expected);
    }

    @Test
    public void shouldNotAddWhenInvalidQty(){
        //given
        initializeCatalogWithSampleProduct();
        //when
        var expected = ProductService.getInstance().validateAdditionQuantity("TEST", 1);
        //then
        assertFalse(expected);
    }

    @Test
    public void shouldNotAddWhenQtyMoreThanStock(){
        //given
        initializeCatalogWithSampleProduct();
        //when
        var expected = ProductService.getInstance().validateAdditionQuantity("99", 1);
        //then
        assertFalse(expected);
    }

    @Test
    public void shouldNotAddNonExistingProduct(){
        //given
        initializeCatalogWithSampleProduct();
        //when
        var expected = ProductService.getInstance().validateAdditionQuantity("1", -99);
        //then
        assertFalse(expected);
    }

    @Test
    public void shouldNotRemoveMoreThanCartAmount() {
        //given
        initializeCatalogWithSampleProduct();
        var cart = Cart.getInstance();
        cart.addProduct(1, 1);
        //when
        var expected = ProductService.getInstance().validateRemovalQuantity("3", 1);
        //then
        assertFalse(expected);
    }

    @Test
    public void shouldNotRemoveItemNotInCart() {
        //given
        var cart = Cart.getInstance();
        //when
        var expected = ProductService.getInstance().validateRemovalQuantity("3", 1);
        //then
        assertFalse(expected);
        assertEquals(Optional.empty(), cart.getCartQuantity(1));
    }

}
