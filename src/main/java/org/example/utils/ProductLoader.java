package org.example.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Catalog;
import org.example.product.Product;

import java.io.File;
import java.util.List;

public class ProductLoader {
    private static final String PRODUCTS_PATH = "src/main/resources/products.json";

    public static void loadProducts() {
        ObjectMapper objectMapper = new ObjectMapper();
        Catalog catalog = Catalog.getInstance();
        try {
            List<Product> loaded = objectMapper.readValue(new File(PRODUCTS_PATH),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));

            catalog.getProductList().addAll(loaded);
        } catch (Exception e) {
            System.out.println("Error while loading products: " + e.getMessage());
        }
    }
}
