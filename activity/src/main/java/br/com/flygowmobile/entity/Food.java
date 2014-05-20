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
            RepositoryFood.Foods.COLUMN_NAME_CATEGORY_ID
    };

    private long foodId;
    private String name;
    private Double value;
    private String description;
    private String nutritionalInfo;
    private boolean isActive;
    private int categoryId;

    public Food(){

    }

    public Food(long foodId, String name, Double value, String description, String nutritionalInfo,boolean isActive, int categoryId){
        this.foodId = foodId;
        this.name = name;
        this.value = value;
        this.description = description;
        this.nutritionalInfo = nutritionalInfo;
        this.isActive = isActive;
        this.categoryId = categoryId;
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

    public String toJSON() {
        return "{" +
                "\"foodId\": "+ getFoodId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"value\": " + getValue() + ", " +
                "\"description\": " + "\"" + getDescription() + " " +
                "\"nutritionalInfo\": " + "\"" + getNutritionalInfo() + " " +
                "\"isActive\": " + "\"" + isActive() + " " +
                "\"categoryId\": " + "\"" + getCategoryId() + " " +
                "}";
    }
}
