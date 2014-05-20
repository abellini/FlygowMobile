package br.com.flygowmobile.entity;

import java.io.Serializable;
import java.util.Date;

import br.com.flygowmobile.database.RepositoryAdvertisement;
import br.com.flygowmobile.database.RepositoryAttendant;

public class Advertisement implements Serializable{
    private int advertisementId;
    private long tabletId;
    private String name;
    private Date inicialDate;
    private Date finalDate;
    private boolean active;
    private String photoName;
    private String videoName;

    public static String[] columns = new String[] {
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_ADVERTISEMENT_ID,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_TABLET_ID,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_NAME,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_INITIAL_DATE,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_FINAL_DATE,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_IS_ACTIVE,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_PHOTO,
            RepositoryAdvertisement.Advertisements.COLUMN_NAME_VIDEO
    };

    public Advertisement() { }

    public Advertisement(int advertisementId, long tabletId, String name, Date inicialDate, Date finalDate, boolean active) {
        this.advertisementId = advertisementId;
        this.tabletId = tabletId;
        this.name = name;
        this.inicialDate = inicialDate;
        this.finalDate = finalDate;
        this.active = active;
    }

    public Advertisement(int advertisementId, String name, Date inicialDate, Date finalDate, boolean active, String photoName, String videoName) {
        this.advertisementId = advertisementId;
        this.name = name;
        this.inicialDate = inicialDate;
        this.finalDate = finalDate;
        this.active = active;
        this.photoName = photoName;
        this.videoName = videoName;
    }

    public int getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(int id) {
        this.advertisementId = id;
    }

    public long getTabletId() {return tabletId; }

    public void setTabletId(long tabletId) {this.tabletId = tabletId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInicialDate() {
        return inicialDate;
    }

    public void setInicialDate(Date inicialDate) {
        this.inicialDate = inicialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
