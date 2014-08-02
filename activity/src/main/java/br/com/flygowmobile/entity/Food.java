package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryFood;

public class Food extends Product {

    public static String[] columns = new String[] {
            RepositoryFood.Foods.COLUMN_NAME_FOOD_ID,
            RepositoryFood.Foods.COLUMN_NAME_NAME,
            RepositoryFood.Foods.COLUMN_NAME_VALUE,
            RepositoryFood.Foods.COLUMN_NAME_DESCRIPTION,
            RepositoryFood.Foods.COLUMN_NAME_NUTRITIONAL_INFO,
            RepositoryFood.Foods.COLUMN_NAME_IS_ACTIVE,
            RepositoryFood.Foods.COLUMN_NAME_CATEGORY_ID,
            RepositoryFood.Foods.COLUMN_NAME_PHOTO,
            RepositoryFood.Foods.COLUMN_NAME_PHOTO_NAME,
            RepositoryFood.Foods.COLUMN_NAME_VIDEO_NAME
    };

    private long foodId;
    private String nutritionalInfo;
    private int categoryId;

    public Food(){

    }

    public Food(long foodId, String name, Double value, String description, String nutritionalInfo, boolean isActive, int categoryId, byte[] photo, String photoName, String videoName) {
        super(name, value, description, isActive, photo, photoName, videoName);
        this.foodId = foodId;
        this.nutritionalInfo = nutritionalInfo;
        this.categoryId = categoryId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(String nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String toJSON() {
        return "{" +
                "\"foodId\": "+ getFoodId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"value\": " + getValue() + ", " +
                "\"description\": " + "\"" + getDescription() + ", " +
                "\"nutritionalInfo\": " + "\"" + getNutritionalInfo() + ", " +
                "\"isActive\": " + "\"" + isActive() + ", " +
                "\"categoryId\": " + "\"" + getCategoryId() + ", " +
                "\"photo\": " + "\"" + getPhoto() + ", " +
                "\"photoName\": " + "\"" + getPhotoName() + ", " +
                "\"videoName\": " + "\"" + getVideoName() + " " +
                "}";
    }
}
