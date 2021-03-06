package br.com.flygowmobile.activity.navigationdrawer;

import java.io.Serializable;

/**
 * Created by Tiago Rocha Gomes on 29/05/14.
 */
public class RowItem implements Serializable {
    private long id;
    private String title;
    private String promoItems;
    private String subtitle;
    private String price;
    private int icon;
    private byte[] image;
    private boolean isGroupHeader = false;
    private boolean isPromoItem = false;
    private boolean isTitle = false;

    public RowItem(String title, int icon, boolean isGroupHeader) {
        this.title = title;
        this.icon = icon;
        this.isGroupHeader = isGroupHeader;
    }

    public RowItem(long id, String title, int icon, boolean isGroupHeader) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.isGroupHeader = isGroupHeader;
    }

    public RowItem(String title, boolean isGroupHeader) {
        this.title = title;
        this.isGroupHeader = isGroupHeader;
        this.isTitle = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromoItems() {
        return promoItems;
    }

    public void setPromoItems(String promoItems) {
        this.promoItems = promoItems;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isPromoItem() {
        return isPromoItem;
    }

    public void setPromoItem(boolean isPromoItem) {
        this.isPromoItem = isPromoItem;
    }

    public boolean isTitle() { return isTitle; }
}
