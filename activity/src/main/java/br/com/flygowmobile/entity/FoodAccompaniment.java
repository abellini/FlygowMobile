package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryFoodAccompaniment;

public class FoodAccompaniment implements Serializable {

    public static String[] columns = new String[]{
            RepositoryFoodAccompaniment.FoodAccompaniments.COLUMN_NAME_FOOD_ID,
            RepositoryFoodAccompaniment.FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID
    };

    private long accompanimentId;
    private long foodId;

    public long getAccompanimentId() {
        return accompanimentId;
    }

    public void setAccompanimentId(long accompanimentId) {
        this.accompanimentId = accompanimentId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }
}
