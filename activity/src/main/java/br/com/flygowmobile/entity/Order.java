package br.com.flygowmobile.entity;

import java.sql.Time;
import java.util.Date;

import br.com.flygowmobile.database.RepositoryOrder;

public class Order {

    public static String[] columns = new String[]{
            RepositoryOrder.Orders.COLUMN_NAME_ORDER_ID,
            RepositoryOrder.Orders.COLUMN_NAME_CLIENT_ID,
            RepositoryOrder.Orders.COLUMN_NAME_TABLET_ID,
            RepositoryOrder.Orders.COLUMN_NAME_TOTAL_VALUE,
            RepositoryOrder.Orders.COLUMN_NAME_ORDER_HOUR,
            RepositoryOrder.Orders.COLUMN_NAME_ATTENDANT_ID
    };

    private long orderId;
    private int clientId;
    private long tabletId;
    private Double totalValue;
    private Date hour;
    private int attendantId;

    public Order(long orderId, int clientId, long tabletId, Double totalValue, Time hour, int attendantId) {
        this.setOrderId(orderId);
        this.setClientId(clientId);
        this.setTabletId(tabletId);
        this.setTotalValue(totalValue);
        this.setHour(hour);
        this.setAttendantId(attendantId);
    }

    public Order() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public long getTabletId() {
        return tabletId;
    }

    public void setTabletId(long tabletId) {
        this.tabletId = tabletId;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public int getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(int attendantId) {
        this.attendantId = attendantId;
    }

    public String toJSONInitialConfig() {

        return "{" +
                "\"orderId\": " + getOrderId() + ", " +
                "\"clientId\": " + "\"" + getClientId() + "\", " +
                "\"tabletId\": " + getTabletId() + ", " +
                "\"totalValue\": " + "\"" + getTotalValue() + "\", " +
                "\"hour\": " + "\"" + getHour() + "\", " +
                "\"attendantId\": " + getAttendantId() +
                "}";
    }
}
