package org.example.filters;

import org.example.Product;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public interface Criteria {
    Predicate<Product> toPredicate();
    Optional<Comparator<Product>> toComparator();
}

