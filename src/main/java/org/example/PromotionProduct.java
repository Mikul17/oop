package org.example;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PromotionProduct extends Product{
    private double promotionPrice;

    PromotionProduct(Product product, double promotionPrice){
        super(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                product.isAvailable());
        this.promotionPrice = promotionPrice;
    }

    @Override
    public double getPrice() {
        return promotionPrice;
    }

    public double getOriginalPrice(){
        return super.getPrice();
    }

}
