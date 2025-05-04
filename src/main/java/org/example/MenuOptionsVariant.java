package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MenuOptionsVariant {
    MAIN_MENU(1),
    CART_MENU(2);

    public final int value;
}
