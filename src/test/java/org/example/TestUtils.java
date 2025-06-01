package org.example;

import org.example.product.Product;
import org.example.product.ProductCategory;

public class TestUtils {

    public static Product sampleProduct() {
        return new Product(1,
                "Test 1",
                10,
                ProductCategory.TSHIRT,
                5,
                true);
    }

    public static Product sampleProductWithId(int id) {
        return new Product(
                id,
                "Test 1",
                10,
                ProductCategory.TSHIRT,
                5,
                true
        );
    }

    public static void initializeCatalogWithSampleProduct() {
        Catalog.getInstance().getProductList().add(sampleProduct());
    }
}
