package org.example.product;

import lombok.Getter;
import org.example.Cart;
import org.example.Catalog;

import java.util.Optional;

public class ProductService {
    @Getter
    private static final ProductService instance = new ProductService();

    private ProductService(){}

    public boolean validateAdditionQuantity(String value, int productId){
        try{
            int quantity = Integer.parseInt(value);
            Product product = Catalog.getInstance().getProductById(productId);

            if(product == null){
                System.out.println("Product with id " + productId + " does not exist. (Press Enter to continue)");
                return false;
            }


            if(quantity < 0 || quantity > product.getStock()){
                System.out.println("Invalid quantity. (Press Enter to continue)");
                return false;
            }

            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public boolean validateRemovalQuantity(String value, int productId){
        try{
            int quantity = Integer.parseInt(value);
            Cart.getInstance().restoreMap();
            Optional<Integer> cardQuantity = Cart.getInstance().getCartQuantity(productId);

            if(cardQuantity.isEmpty()){
                System.out.println("Product with id " + productId + " is not in cart. (Press Enter to continue)");
                return false;
            }

            if(quantity < 0 || cardQuantity.get() < quantity){
                System.out.println("Invalid quantity. (Press Enter to continue)");
                return false;
            }

            return true;

        }catch (NumberFormatException e){
            return false;
        }
    }

}
