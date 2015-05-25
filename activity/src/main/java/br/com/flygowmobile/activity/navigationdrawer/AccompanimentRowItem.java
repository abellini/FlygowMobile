package br.com.flygowmobile.activity.navigationdrawer;

import java.io.Serializable;

/**
 * Created by Tiago Rocha Gomes on 29/05/14.
 */
public class AccompanimentRowItem implements Serializable {
    private long id;
    private String title;
    private String subtitle;
    private String price;
    private int icon;

    public AccompanimentRowItem(){}

    public AccompanimentRowItem(long id, String title, String subtitle, String price, int icon) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.icon = icon;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
