package br.com.flygowmobile.entity;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.flygowmobile.database.RepositoryOrderItem;

public class OrderItem {

    public static String[] columns = new String[]{
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ITEM_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_QUANTITY,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_OBSERVATIONS,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_VALUE,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_FOOD_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_PRODUCT_TYPE
    };

    private long orderItemId;
    private int quantity;
    private String observations;
    private Double value;
    private long foodId;
    private long orderId;
    private String productType;

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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String toJSONInitialConfig() {
        return "{" +
                "\"orderItemId\": " + getOrderItemId() + ", " +
                "\"quantity\": " + "\"" + getQuantity() + "\", " +
                "\"observations\": " + "\"" + null + "\", " +
                "\"value\": " + "\"" + getValue() + "\", " +
                "\"foodId\": " + "\"" + getFoodId() + "\", " +
                "\"orderId\": " + getOrderId() + "\", " +
                "\"productType\": " + "\"" + getProductType() + "\" " +
                "}";
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderItemId", getOrderItemId());
            jsonObject.put("quantity", getQuantity());
            jsonObject.put("observations", getObservations());
            jsonObject.put("value", getValue());
            jsonObject.put("foodId", getFoodId());
            jsonObject.put("orderId", getOrderId());
            jsonObject.put("productType", getProductType());
            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
