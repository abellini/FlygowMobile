package br.com.flygowmobile.entity;


import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryCategory;

public class Category implements Serializable {

    public static String[] columns = new String[] {
            RepositoryCategory.Categories.COLUMN_NAME_CATEGORY_ID,
            RepositoryCategory.Categories.COLUMN_NAME_NAME,
            RepositoryCategory.Categories.COLUMN_NAME_DESCRIPTION,
            RepositoryCategory.Categories.COLUMN_NAME_PHOTO
    };

    private long categoryId;
    private String name;
    private String description;
    private byte[] photo;

    public Category() {}

    public Category(long categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String toJSONInitialConfig() {
        return "{" +
                "\"categoryId\": "+ getCategoryId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"description\": " + "\"" + getDescription() + "\"" +
                "}";
    }
}
