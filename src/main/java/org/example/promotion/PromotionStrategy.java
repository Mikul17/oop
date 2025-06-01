package org.example.promotion;

import org.example.product.Product;

import java.util.Map;

public sealed interface PromotionStrategy
        permits PromotionCode {
    Map<Product, Integer> apply(Map<Product, Integer> cart);
}