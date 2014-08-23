package br.com.flygowmobile.activity.navigationdrawer;

import java.io.Serializable;

/**
 * Created by alexandre on 8/13/14.
 */
public class OrderRowItem implements Serializable {
    private long id;
    private int icon;
    private String title;
    private String observations;
    private int quantity;
    private Double priceUnit;
    private Double priceTotal;

    public OrderRowItem() {
    }

    public OrderRowItem(long id, int icon, String title, String observations, int quantity, Double priceUnit) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.observations = observations;
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.priceTotal = (priceUnit * quantity);

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(Double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }
}
