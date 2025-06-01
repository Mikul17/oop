package org.example;


import static org.example.menu.MenuPrinter.runMenu;
import static org.example.utils.ProductLoader.loadProducts;

public class Main {
    public static void main(String[] args) {
        loadProducts();
        runMenu();
    }
}