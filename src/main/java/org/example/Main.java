package org.example;


import static org.example.MenuPrinter.runMenu;
import static org.example.ProductLoader.loadProducts;

public class Main {
    public static void main(String[] args) {
        loadProducts();
        runMenu();
    }
}