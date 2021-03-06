package br.com.flygowmobile.activity.navigationdrawer;

import java.io.Serializable;

import br.com.flygowmobile.Utils.FunctionUtils;

/**
 * Created by alexandre on 8/13/14.
 */
public class OrderRowItem implements Serializable {
    private long id;
    private long orderItemId;
    private int icon;
    private String title;
    private String observations;
    private int quantity;
    private int status;
    private Double priceUnit;
    private Double priceTotal;

    private static String UNIT_SULFIX = "un.";

    public OrderRowItem() {
    }

    public OrderRowItem(long orderItemId, long id, int icon, String title, String observations, int quantity, Double priceUnit) {
        this.orderItemId = orderItemId;
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.observations = observations;
        this.quantity = quantity;
        this.priceUnit = priceUnit;
        this.priceTotal = (priceUnit * quantity);

    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
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

    public String getQuantity() {
        return FunctionUtils.addZero(quantity) + " " + UNIT_SULFIX;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
