package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.activity.SplashScreen;
import br.com.flygowmobile.database.RepositoryFood;
import br.com.flygowmobile.database.RepositoryPromotion;

/**
 * Created by Tiago Rocha Gomes on 03/06/14.
 */
public class Promotion implements Serializable {

    public static String[] columns = new String[] {
        RepositoryPromotion.Promotions.COLUMN_NAME_PROMOTION_ID,
        RepositoryPromotion.Promotions.COLUMN_NAME_NAME,
        RepositoryPromotion.Promotions.COLUMN_NAME_DESCRIPTION,
        RepositoryPromotion.Promotions.COLUMN_NAME_VALUE,
        RepositoryPromotion.Promotions.COLUMN_NAME_PHOTO,
        RepositoryPromotion.Promotions.COLUMN_NAME_VIDEO
    };

    private int promotionId;
    private String name;
    private String description;
    private Double value;
    private byte[] photo;
    private byte[] video;

    public Promotion() {
    }

    public Promotion(int promotionId, String name, String description, Double value) {
        this.promotionId = promotionId;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }
}
