package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.activity.SplashScreen;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryPromotion;

/**
 * Created by Tiago Rocha Gomes on 03/06/14.
 */
public class Promotion extends Product{

    public static String[] columns = new String[] {
        RepositoryPromotion.Promotions.COLUMN_NAME_PROMOTION_ID,
        RepositoryPromotion.Promotions.COLUMN_NAME_NAME,
        RepositoryPromotion.Promotions.COLUMN_NAME_DESCRIPTION,
        RepositoryPromotion.Promotions.COLUMN_NAME_VALUE,
        RepositoryPromotion.Promotions.COLUMN_NAME_PHOTO_NAME,
        RepositoryPromotion.Promotions.COLUMN_NAME_PHOTO,
        RepositoryPromotion.Promotions.COLUMN_NAME_VIDEO_NAME
    };

    private int promotionId;

    public Promotion() {
    }

    public Promotion(int promotionId, String name, String description, Double value) {
        super(name, value, description);
        this.promotionId = promotionId;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }
}
