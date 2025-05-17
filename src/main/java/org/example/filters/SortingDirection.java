package org.example.filters;

public enum SortingDirection {
    ASCENDING, DESCENDING, UNORDERED;

    public static SortingDirection from(int value) {
        return switch (value) {
            case 1 -> ASCENDING;
            case 2 -> DESCENDING;
            default -> UNORDERED;
        };
    }
}
