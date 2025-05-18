package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.filters.ProductCriteria;
import org.example.filters.SortField;
import org.example.filters.SortingDirection;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PromotionCode implements PromotionStrategy {
    TEN_PERCENT(
            "10% discount on all products",
            "DZIESIONA") {

        @Override
        public Map<Product, Integer> apply(Map<Product, Integer> cart) {
            List<Product> products = MapUtils.collapsToList(cart);

            List<Product> promotionProducts = products.stream()
                    .map(product -> new PromotionProduct(
                                    product, product.getPrice() - (product.getPrice() * 0.1)
                            )
                    ).collect(Collectors.toList());

            return MapUtils.collapsToMap(promotionProducts);
        }
    },

    CHEAPEST_FOR_ONE(
            "When buying 3 products, get cheapest one for 1 PLN",
            "3_ZA_1") {

        @Override
        public Map<Product, Integer> apply(Map<Product, Integer> cart) {
            List<Product> products = MapUtils.collapsToList(cart);

            int toBeDiscounted = products.size() / 3;

            var comparator = ProductCriteria.sortBy(SortField.PRICE, SortingDirection.ASCENDING).toComparator().orElseThrow();

            List<Product> sortedProducts = products.stream().sorted(comparator).toList();

            var result = Stream.concat(
                    sortedProducts.stream()
                            .limit(toBeDiscounted)
                            .map(product -> new PromotionProduct(product, 1.0)),
                    sortedProducts.stream().skip(toBeDiscounted)
            ).toList();


            return MapUtils.collapsToMap(result);
        }
    },

    ONE_FOR_HALF(
            "When buying 2 identical products, second one is 50% off",
            "DWOJA") {


        @Override
        public Map<Product, Integer> apply(Map<Product, Integer> cart) {
            List<Product> suitable = new ArrayList<>();
            for(Map.Entry<Product, Integer> entry : cart.entrySet()) {
                int amount = entry.getValue() /2 ;
                for(int i=0; i<amount; i++) {
                    var discounted = new PromotionProduct(entry.getKey(), entry.getKey().getPrice() /2);
                    suitable.add(discounted);
                    cart.put(entry.getKey(), entry.getValue()-1);
                }
            }
            var collapsed = MapUtils.collapsToList(cart);

            suitable.addAll(collapsed);

            return MapUtils.collapsToMap(suitable);
        }
    };

    private final String description;
    private final String promotionCode;

    public static final Map<String, PromotionCode> BY_CODE = Collections.unmodifiableMap(
            Arrays.stream(values())
                    .collect(Collectors.toMap(PromotionCode::getPromotionCode, Function.identity()))
    );

    public static Optional<PromotionCode> from(String code) {
        return Optional.ofNullable(BY_CODE.get(code));
    }
}
