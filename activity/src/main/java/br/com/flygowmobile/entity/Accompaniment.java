package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryAccompaniment;

public class Accompaniment implements Serializable {

    public static String[] columns = new String[] {
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_NAME,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_VALUE,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_DESCRIPTION,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_IS_ACTIVE,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_CATEGORY_ID
    };
    private int accompanimentId;
    private String name;
    private Double value;
    private String description;
    private boolean active;
    private int categoryId;

    public Accompaniment() {}

    public Accompaniment(int accompanimentId, String name, Double value, String description, boolean active, int categoryId) {
        this.accompanimentId = accompanimentId;
        this.name = name;
        this.value = value;
        this.description = description;
        this.active = active;
        this.categoryId = categoryId;
    }

    public int getAccompanimentId() {
        return accompanimentId;
    }

    public void setAccompanimentId(int accompanimentId) {
        this.accompanimentId = accompanimentId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String toJSON() {
        return "{" +
                "\"foodId\": "+ getAccompanimentId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"value\": " + getValue() + ", " +
                "\"description\": " + "\"" + getDescription() + "\", " +
                "\"isActive\": " + "\"" + isActive() + "\", " +
                "\"categoryId\": " + getCategoryId()  +
                "}";
    }
}
