package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryFoodPromotion;

public class FoodPromotion implements Serializable {

    public static String[] columns = new String[]{
            RepositoryFoodPromotion.FoodPromotions.COLUMN_NAME_FOOD_ID,
            RepositoryFoodPromotion.FoodPromotions.COLUMN_NAME_PROMOTION_ID
    };

    private long promotionId;
    private long foodId;

    public FoodPromotion() {
    }

    public FoodPromotion(long promotionId, long foodId) {
        this.promotionId = promotionId;
        this.foodId = foodId;
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }
}
