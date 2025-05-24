package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;


class PromotionTest {


    @Test
    void shouldApplyOneForHalfPromotionCode() {
      //given
        var product1 = new Product(1, "Test 1", 10, ProductCategory.TSHIRT, 5, true);
        var discountedProduct = new PromotionProduct(product1, 5);
        var map = new HashMap<>(Map.of(product1, 2));

        //when
        var result = PromotionCode.ONE_FOR_HALF.apply(map);

        //then
        Assertions.assertEquals(1, map.size());
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.containsKey(discountedProduct));
    }

    @Test
    void shouldNotApplyOneForHalfPromotionCodeForDifferentProducts() {
        //given
        var product1 = new Product(1, "Test 1", 10, ProductCategory.TSHIRT, 5, true);
        var product2 = new Product(2, "Test 2", 10, ProductCategory.HOODIE, 5, true);
        var discountedProduct = new PromotionProduct(product1, 5);
        var discountedProduct2 = new PromotionProduct(product2, 5);
        var map = new HashMap<>(Map.of(product1, 1, product2, 1));

        //when
        var result = PromotionCode.ONE_FOR_HALF.apply(map);

        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals(2, result.size());
        Assertions.assertFalse(result.containsKey(discountedProduct));
        Assertions.assertFalse(result.containsKey(discountedProduct2));
    }

    @Test
    void shouldApplyCheapestForOnePromotionCode() {
        //given
        var product1 = new Product(1, "Test 1", 10, ProductCategory.TSHIRT, 5, true);
        var discountedProduct = new PromotionProduct(product1, 1);
        var map = new HashMap<>(Map.of(product1, 3));

        //when
        var result = PromotionCode.CHEAPEST_FOR_ONE.apply(map);

        //then
        Assertions.assertEquals(1, map.size());
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.containsKey(discountedProduct));
    }

}