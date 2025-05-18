package org.example.filters;

import org.example.Product;
import org.example.ProductCategory;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class ProductCriteria {
        record GenericCriteria(Predicate<Product> predicate, Comparator<Product> comparator) implements Criteria {
            @Override
            public Predicate<Product> toPredicate() {
                return predicate;
            }

            @Override
            public Optional<Comparator<Product>> toComparator() {
                return Optional.ofNullable(comparator);
            }
        }

    public static Criteria categoryIn(Set<ProductCategory> categories) {
        return new GenericCriteria(product -> categories.contains(product.getCategory()), null);
    }

    public static Criteria sortBy(SortField sortField, SortingDirection direction) {
            return new GenericCriteria(
                    product -> true,
                    sortField.comparatorFor(direction));
    }
}
