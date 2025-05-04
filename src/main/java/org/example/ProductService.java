package org.example;

import lombok.Getter;

public class ProductService {
    @Getter
    private static final ProductService instance = new ProductService();

    private ProductService(){

    }
    
//    public static boolean addProductToCart(Cart cart, Product product){
//        return cart.getCart().add(product);
//    }


    public boolean validateQuantity(String value, int productId){
        try{
            int quantity = Integer.parseInt(value);
            Product product = Catalog.getInstance().getProductById(productId);

            if(product == null){
                return false;
            }

            return quantity > 0 && quantity <= product.getStock();
        }catch (NumberFormatException e){
            return false;
        }
    }


}
