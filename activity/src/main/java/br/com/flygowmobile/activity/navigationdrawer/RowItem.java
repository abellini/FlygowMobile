package br.com.flygowmobile.activity.navigationdrawer;

/**
 * Created by Tiago Rocha Gomes on 29/05/14.
 */
public class RowItem {
    private String title;
    private String promoItems;
    private String subtitle;
    private String price;
    private int icon;
    private byte[] image;
    private boolean isGroupHeader = false;
    private boolean isPromoItem = false;

    public RowItem(String title, int icon, boolean isGroupHeader) {
        this.title = title;
        this.icon = icon;
        this.isGroupHeader = isGroupHeader;
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
}
