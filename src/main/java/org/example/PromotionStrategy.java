package org.example;

import java.util.Map;

public sealed interface PromotionStrategy
        permits PromotionCode {
    Map<Product, Integer> apply(Map<Product, Integer> cart);
}