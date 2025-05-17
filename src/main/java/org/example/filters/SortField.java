package org.example.filters;

import lombok.RequiredArgsConstructor;
import org.example.Product;

import java.util.Comparator;

@RequiredArgsConstructor
public enum SortField {
    NAME(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER)),
    PRICE(Comparator.comparing(Product::getPrice));

    private final Comparator<Product> baseComparator;

    public Comparator<Product> comparatorFor(SortingDirection direction) {
        return direction == SortingDirection.ASCENDING ?
                this.baseComparator :
                this.baseComparator.reversed();
    }
}
