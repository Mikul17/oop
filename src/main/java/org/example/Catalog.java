package org.example;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Catalog {

    @Getter
    private static final Catalog instance = new Catalog();
    private final List<Product> productList = new ArrayList<>();

    public List<Product> getProductListByCategory(ProductCategory category) {
        return productList.stream()
                .filter(product -> product.getCategory().equals(category))
                .filter(Product::isAvailable)
                .sorted(Comparator.comparing(Product::getPrice))
                .toList();
    }

    public Product getProductById(int id) {
        return productList.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
