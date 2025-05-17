package org.example;

import org.example.filters.Criteria;
import org.example.filters.ProductCriteria;
import org.example.filters.SortField;
import org.example.filters.SortingDirection;

import java.text.DecimalFormat;
import java.util.*;

public class MenuPrinter {
    private static final int WIDTH = 80;
    private static final List<String> catalogHeaders = List.of("Id", "Name", "Price", "Category", "Stock");
    private static final List<String> cartHeaders = List.of("Id", "Name", "Price", "Category", "Quantity");
    private static MenuOptionsVariant currentOptionsMenuVariant = MenuOptionsVariant.MAIN_MENU;
    private static Criteria sortingCriteria = null;
    private static Criteria filterCriteria = null;
    private static final DecimalFormat df = new DecimalFormat("#.00");

    public static void runMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            int choice;
            if (currentOptionsMenuVariant == MenuOptionsVariant.MAIN_MENU) {
                choice = getValidChoice(scanner);
                switch (choice) {
                    case 1 -> {
                        productTableModifyMenu(scanner);
                    }
                    case 2 -> handleAddToCart(scanner);
                    case 3 -> currentOptionsMenuVariant = MenuOptionsVariant.CART_MENU;
                    case 4 -> {
                        if (Cart.getInstance().getAmountOfProducts() > 0) {
                            System.out.println("\nYou must remove items from your cart first! (Press Enter to continue)");
                            scanner.nextLine();
                        } else {
                            running = false;
                            System.out.println("\nExiting... (Press Enter to continue)");
                            scanner.nextLine();
                        }
                    }
                }
            } else if (currentOptionsMenuVariant == MenuOptionsVariant.CART_MENU) {
                choice = getValidChoice(scanner);
                switch (choice) {
                    case 1 -> handleRemoveFromCart(scanner);
                    case 2 -> {
                        Cart.getInstance().clearCart();
                        System.out.println("\nCart cleared. (Press Enter to continue)");
                        scanner.nextLine();
                    }
                    case 3 -> {
                        Cart.getInstance().applyDiscount(0.5);
                        System.out.println("\nDiscount applied. (Press Enter to continue)");
                        scanner.nextLine();
                    }
                    case 4 -> currentOptionsMenuVariant = MenuOptionsVariant.MAIN_MENU;
                }
            }
        }
        scanner.close();
    }

    public static void printMenu() {
        printFrameTop();
        printProductTable();
        printOptionsMenu();
        printFrameBottom();

    }

    private static void printProductTable() {
        if (currentOptionsMenuVariant == MenuOptionsVariant.MAIN_MENU) {
            printCatalogTable();
        } else {
            printCartTable(Cart.getInstance().getCartItems());
        }
    }

    private static void printCatalogTable() {
        List<List<String>> rows = new ArrayList<>();
        var products = Catalog.getInstance().getFilteredProductList(handleFilterChange());
        for (Product p : products) {
            rows.add(List.of(
                    String.valueOf(p.getId()),
                    p.getName(),
                    String.valueOf(p.getPrice()),
                    String.valueOf(p.getCategory()),
                    String.valueOf(p.getStock())
            ));
        }
        printTable(catalogHeaders, rows);
    }

    private static void productTableModifyMenu(Scanner scanner) {
        System.out.println("Modify display table:");
        System.out.println("1. Order by name");
        System.out.println("2. Order by price");
        System.out.println("3. Filter by category");
        System.out.println("4. Clear filters");
        String input = scanner.nextLine();
        try{
            int choice1 = Integer.parseInt(input);
            int choice2 = -1;
            if(choice1 < 3) {
                System.out.println("1. Ascending");
                System.out.println("2. Descending");
                input = scanner.nextLine();
                choice2= Integer.parseInt(input);
            }
            var order = SortingDirection.from(choice2);
            switch (choice1) {
                case 1: {
                    sortingCriteria = ProductCriteria.sortBy(SortField.NAME, order);
                    System.out.println("Order by name applied. (Press Enter to continue)");
                    break;
                }
                case 2: {
                    sortingCriteria = ProductCriteria.sortBy(SortField.PRICE, order);
                    System.out.println("Order by price applied. (Press Enter to continue)");
                    break;
                }
                case 3:  {
                    printProductFiltering(scanner);
                    break;
                }
                case 4: {
                    sortingCriteria = null;
                    filterCriteria = null;
                    break;
                }
                default: {
                    System.out.println("Invalid choice. (Press Enter to continue)");
                    break;
                }
            }
        }catch (NumberFormatException e){
            System.out.println("Invalid choice. (Press Enter to continue)");
        }
        scanner.nextLine();
    }

    private static void printProductFiltering(Scanner scanner) {
        System.out.println("Select category:");
        int i = 1;
        for (ProductCategory c : ProductCategory.values()) {
            System.out.println(i+". " + c.name());
            i++;
        }
        String input = scanner.nextLine();
        try {
            int choice = Integer.parseInt(input);
            ProductCategory category = ProductCategory.values()[choice - 1];
            filterCriteria = ProductCriteria.categoryIn(Set.of(category));
            System.out.println("Selected category: " + category.name() + " (Press Enter to continue)");
        } catch (NumberFormatException | IndexOutOfBoundsException e){
            System.out.println("Invalid choice. (Press Enter to continue)");
        }
    }

    private static void printCartTable(Map<Product, Integer> cartItems) {
        List<List<String>> rows = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product p = entry.getKey();
            int quantity = entry.getValue();
            rows.add(List.of(
                    String.valueOf(p.getId()),
                    p.getName(),
                    String.valueOf(p.getPrice()),
                    String.valueOf(p.getCategory()),
                    String.valueOf(quantity)
            ));
        }
        printTable(cartHeaders, rows);
    }

    private static int getValidChoice(Scanner scanner) {
        int choice;
        int maxChoice = currentOptionsMenuVariant == MenuOptionsVariant.MAIN_MENU ? getMainMenuOptions().size() : getCartMenuOptions().size();
        while (true) {
            System.out.print("\nChoose an option (" + 1 + "-" + maxChoice + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= maxChoice) {
                    break;
                } else {
                    System.out.println("Invalid choice. Enter a number from " + 1 + " to " + maxChoice + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Enter a number from " + 1 + " to " + maxChoice + ".");
            }
        }
        return choice;
    }

    private static void handleAddToCart(Scanner scanner) {
        System.out.print("Enter product Id to add to cart: ");
        String input = scanner.nextLine();
        try {
            int productId = Integer.parseInt(input);
            System.out.println("Enter quantity: ");
            String qty = scanner.nextLine();

            if(ProductService.getInstance().validateAdditionQuantity(qty, productId)) {
                Cart.getInstance().addProduct(productId, Integer.parseInt(qty));
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid product Id. (Press Enter to continue)");
        }
        scanner.nextLine();
    }

    private static void handleRemoveFromCart(Scanner scanner) {
        System.out.print("Enter product Id to remove from cart: ");
        String input = scanner.nextLine();
        try {
            int productId = Integer.parseInt(input);
            System.out.println("Enter quantity: ");
            String qty = scanner.nextLine();

            if(ProductService.getInstance().validateRemovalQuantity(qty, productId)) {
                Cart.getInstance().removeProduct(productId, Integer.parseInt(qty));
                System.out.println("Removed "+ qty +" product(s) with Id "+ productId +" (Press Enter to continue)");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid product Id. (Press Enter to continue)");
        }
        scanner.nextLine();
    }

    //Utility

    private static List<Criteria> handleFilterChange(){
        List<Criteria> filters = new ArrayList<>();
        if(sortingCriteria != null) {
            filters.add(sortingCriteria);
        }
        if(filterCriteria != null) {
            filters.add(filterCriteria);
        }
        return filters;
    }

    //Options
    private static List<String> getMainMenuOptions() {
        List<String> options = List.of(
                "1. Change table display",
                "2. Add to cart",
                "3. Display Cart",
                "4. Exit"
        );
        System.out.println("Options:");
        return options;
    }

    private static List<String> getCartMenuOptions() {
        List<String> options = List.of(
                "1. Remove from cart",
                "2. Clear cart",
                "3. Apply discount",
                "4. Back to main menu"
        );
        System.out.println("Cart Options:");
        return options;
    }

    private static void printOptionsMenu() {
        List<String> options;
        if (currentOptionsMenuVariant == MenuOptionsVariant.MAIN_MENU) {
            options = getMainMenuOptions();
        } else {
            options = getCartMenuOptions();
        }
        for (String option : options) {
            System.out.println(option);
        }
        for (int i = options.size(); i < 6; i++) {
            System.out.println();
        }
    }


    //Table building
    private static void printTable(List<String> headers, List<List<String>> rows) {
        int numColumns = headers.size();
        int[] minWidths = new int[numColumns];
        for (int i = 0; i < numColumns; i++) {
            minWidths[i] = headers.get(i).length();
        }

        for (List<String> row : rows) {
            for (int i = 0; i < numColumns; i++) {
                minWidths[i] = Math.max(minWidths[i], row.get(i).length());
            }
        }
        int[] colWidths = adjustColumnWidths(minWidths, numColumns);
        String borderLine = buildTableBorder(colWidths);
        String headerRow = buildTableRow(headers, colWidths);
        System.out.println(borderLine);
        System.out.println(headerRow);
        System.out.println(borderLine);
        for (List<String> row : rows) {
            System.out.println(buildTableRow(row, colWidths));
        }
        System.out.println(borderLine);
    }

    private static String buildTableRow(List<String> cells, int[] colWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int i = 0; i < cells.size(); i++) {
            sb.append(centerPad(cells.get(i), colWidths[i]));
            sb.append("|");
        }
        return sb.toString();
    }

    private static String buildTableBorder(int[] colWidths) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int width : colWidths) {
            sb.append("-".repeat(width));
            sb.append("|");
        }
        return sb.toString();
    }

    private static String padLeft(String text) {
        if (text.length() >= WIDTH - 2) return text;
        return " ".repeat(WIDTH - 2 - text.length()) + text;
    }

    private static String centerPad(String text, int width) {
        if (text.length() >= width) return text;
        int totalPadding = width - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    private static String createBorderLine() {
        return "+" + "-".repeat(WIDTH - 2) + "+";
    }

    private static int[] adjustColumnWidths(int[] minWidths, int numColumns) {
        int available = MenuPrinter.WIDTH - (numColumns + 1);
        int sumMin = 0;
        for (int w : minWidths) {
            sumMin += w;
        }
        int extraTotal = available - sumMin;
        int[] colWidths = new int[numColumns];
        int extraPerColumn = extraTotal / numColumns;
        int remainder = extraTotal % numColumns;
        for (int i = 0; i < numColumns; i++) {
            colWidths[i] = minWidths[i] + extraPerColumn + (i < remainder ? 1 : 0);
        }
        return colWidths;
    }

    private static void printFrameTop() {
        String horizontalBorder = createBorderLine();
        System.out.println(horizontalBorder);

        int amountOfProducts = Cart.getInstance().getAmountOfProducts();
        String cartText = "Cart (" + amountOfProducts + ")";

        String totalPrice = "Total Price: $" + df.format(Cart.getInstance().getTotalPrice());
        System.out.println("|" + padLeft(cartText) + "|");
        if(Cart.getInstance().getTotalPrice() > 0){
            System.out.println("|" + padLeft(totalPrice) + "|");
        }

        String welcomeText = "Welcome to Merito Clothing Shop";
        System.out.println("|" + centerPad(welcomeText, WIDTH - 2) + "|");

        System.out.println(horizontalBorder);
    }

    private static void printFrameBottom() {
        String horizontalBorder = createBorderLine();
        System.out.println(horizontalBorder);
    }

}