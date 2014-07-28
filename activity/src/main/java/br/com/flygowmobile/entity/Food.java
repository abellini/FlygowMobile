package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryFood;

public class Food implements Serializable {

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
    private String name;
    private Double value;
    private String description;
    private String nutritionalInfo;
    private boolean isActive;
    private int categoryId;
    private byte[] photo;
    private String photoName;
    private String videoName;

    public Food(){

    }

    public Food(long foodId, String name, Double value, String description, String nutritionalInfo, boolean isActive, int categoryId, byte[] photo, String photoName, String videoName) {
        this.foodId = foodId;
        this.name = name;
        this.value = value;
        this.description = description;
        this.nutritionalInfo = nutritionalInfo;
        this.isActive = isActive;
        this.categoryId = categoryId;
        this.setPhoto(photo);
        this.photoName = photoName;
        this.videoName = videoName;
    }


    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(String nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
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
