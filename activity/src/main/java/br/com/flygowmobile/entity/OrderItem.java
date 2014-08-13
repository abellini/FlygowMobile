package br.com.flygowmobile.entity;

import br.com.flygowmobile.database.RepositoryOrderItem;

public class OrderItem {

    private static String[] columns = new String[]{
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ITEM_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_QUANTITY,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_OBSERVATIONS,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_VALUE,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_FOOD_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ID
    };

    private long orderItemId;
    private int quantity;
    private String observations;
    private Double value;
    private long foodId;
    private long orderId;

    public OrderItem() {
    }

    public OrderItem(long orderItemId, int quantity, String observations, Double value, long foodId, long orderId) {
        this.setOrderItemId(orderItemId);
        this.setQuantity(quantity);
        this.setObservations(observations);
        this.setValue(value);
        this.setFoodId(foodId);
        this.setOrderId(orderId);

    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String toJSONInitialConfig() {
        return "{" +
                "\"orderItemId\": " + getOrderItemId() + ", " +
                "\"quantity\": " + "\"" + getQuantity() + "\", " +
                "\"observations\": " + getObservations() + ", " +
                "\"value\": " + "\"" + getValue() + "\", " +
                "\"foodId\": " + "\"" + getFoodId() + "\", " +
                "\"orderId\": " + getOrderId() +
                "}";
    }
}
