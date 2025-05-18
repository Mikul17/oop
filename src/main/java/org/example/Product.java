package org.example;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private int id;
    @EqualsAndHashCode.Include
    private String name;
    private double price;
    private ProductCategory category;
    private int stock;
    private boolean isAvailable = true;


    @Override
    public String toString() {
        return name + " | " + price +" pln";
    }

    public void updateStock(int quantity) {
        stock += quantity;
        if (stock == 0) {
            isAvailable = false;
            return;
        }
        isAvailable = true;
    }
}
