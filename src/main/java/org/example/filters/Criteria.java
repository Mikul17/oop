package org.example.filters;

import org.example.product.Product;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public sealed interface Criteria permits ProductCriteria.GenericCriteria {
    Predicate<Product> toPredicate();
    Optional<Comparator<Product>> toComparator();
}

