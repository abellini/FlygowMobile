package br.com.flygowmobile.entity;

import java.io.Serializable;

/**
 * Created by Tiago Rocha Gomes on 02/08/14.
 */
public class Product implements Serializable {

    private String name;
    private Double value;
    private String description;
    private boolean isActive;
    private byte[] photo;
    private String photoName;
    private String videoName;


    public Product(
            String name,
            Double value,
            String description,
            boolean isActive,
            byte[] photo,
            String photoName,
            String videoName
    ) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.isActive = isActive;
        this.photo = photo;
        this.photoName = photoName;
        this.videoName = videoName;
    }

    public Product(
            String name,
            Double value,
            String description
    ) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public Product(){}

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
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
}
