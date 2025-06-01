package org.example;

import lombok.*;
import org.example.filters.Criteria;
import org.example.product.Product;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Catalog {

    @Getter
    private static final Catalog instance = new Catalog();
    private final List<Product> productList = new ArrayList<>();

    public List<Product> getFilteredProductList(List<Criteria> criteriaList) {
        if(criteriaList.isEmpty()) {
            return productList.stream().filter(Product::isAvailable).collect(Collectors.toList());
        }
        Predicate<Product> pred = criteriaList.stream()
                .map(Criteria::toPredicate)
                .reduce(p -> true, Predicate::and);

        Optional<Comparator<Product>> comp = criteriaList.stream()
                .map(Criteria::toComparator)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        var copy = Collections.unmodifiableList(productList);
        var stream = copy.stream().filter(pred).filter(Product::isAvailable);
        if (comp.isPresent()) {
            stream = stream.sorted(comp.get());
        }
        return stream.collect(Collectors.toList());
    }

    public Product getProductById(int id) {
        return productList.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
