package org.example.product;

import lombok.*;


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
