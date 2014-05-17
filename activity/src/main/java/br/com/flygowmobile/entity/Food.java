package br.com.flygowmobile.entity;

import br.com.flygowmobile.database.RepositoryFood;

public class Food {

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
}
