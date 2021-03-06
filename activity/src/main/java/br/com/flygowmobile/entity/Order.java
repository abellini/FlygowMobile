package br.com.flygowmobile.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;

import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.database.RepositoryOrder;

public class Order {

    public static String[] columns = new String[]{
            RepositoryOrder.Orders.COLUMN_NAME_ORDER_ID,
            RepositoryOrder.Orders.COLUMN_NAME_ORDER_SERVER_ID,
            RepositoryOrder.Orders.COLUMN_NAME_CLIENT_ID,
            RepositoryOrder.Orders.COLUMN_NAME_TABLET_ID,
            RepositoryOrder.Orders.COLUMN_NAME_TOTAL_VALUE,
            RepositoryOrder.Orders.COLUMN_NAME_ORDER_HOUR,
            RepositoryOrder.Orders.COLUMN_NAME_ATTENDANT_ID,
            RepositoryOrder.Orders.COLUMN_NAME_STATUS_TYPE
    };

    private long orderId;
    private long orderServerId;
    private int clientId;
    private long tabletId;
    private Double totalValue;
    private Date hour;
    private long attendantId;
    private int statusType;

    public Order(long orderId, long orderServerId, int clientId, long tabletId, Double totalValue, Time hour, long attendantId) {
        this.setOrderId(orderId);
        this.setOrderServerId(orderServerId);
        this.setClientId(clientId);
        this.setTabletId(tabletId);
        this.setTotalValue(totalValue);
        this.setHour(hour);
        this.setAttendantId(attendantId);
    }

    public Order() {
    }

    public long getOrderServerId() {
        return orderServerId;
    }

    public void setOrderServerId(long orderServerId) {
        this.orderServerId = orderServerId;
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

    public long getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(long attendantId) {
        this.attendantId = attendantId;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }


    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", getOrderId());
            jsonObject.put("orderServerId", getOrderServerId());
            jsonObject.put("clientId", getClientId());
            jsonObject.put("tabletId", getTabletId());
            jsonObject.put("totalValue", getTotalValue());
            jsonObject.put("date", StringUtils.parseString(getHour()));
            jsonObject.put("attendantId", getAttendantId());
            jsonObject.put("statusType", getStatusType());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
