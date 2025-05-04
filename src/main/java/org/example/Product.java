package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private int id;
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
        if(quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if(quantity > stock) {
            throw new IllegalArgumentException("Not enough stock");
        }

        stock -= quantity;
        if (stock == 0) {
            isAvailable = false;
        }
    }
}
