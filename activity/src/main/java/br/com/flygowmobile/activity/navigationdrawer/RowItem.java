package br.com.flygowmobile.activity.navigationdrawer;

/**
 * Created by Tiago Rocha Gomes on 29/05/14.
 */
public class RowItem {
    private String title;
    private String subtitle;
    private String price;
    private int icon;
    private boolean isGroupHeader = false;

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
}
