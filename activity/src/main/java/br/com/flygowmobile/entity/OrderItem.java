package br.com.flygowmobile.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.flygowmobile.database.RepositoryOrderItem;

public class OrderItem {

    public static String[] columns = new String[]{
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ITEM_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ITEM_SERVER_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_QUANTITY,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_OBSERVATIONS,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_VALUE,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_FOOD_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ID,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_PRODUCT_TYPE,
            RepositoryOrderItem.OrderItems.COLUMN_NAME_STATUS,
    };

    private long orderItemId;
    private long orderItemServerId;
    private long orderId;
    private int quantity;
    private String observations;
    private Double value;
    private long foodId;
    private int status;

    private String productType;
    private List<Accompaniment> accompanimentList;

    public OrderItem() {
    }

    public OrderItem(long orderItemId, long orderItemServerId, int quantity, String observations, Double value, long foodId, long orderId) {
        this.setOrderItemId(orderItemId);
        this.setOrderItemServerId(orderItemServerId);
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

    public long getOrderItemServerId() {
        return orderItemServerId;
    }

    public void setOrderItemServerId(long orderItemServerId) {
        this.orderItemServerId = orderItemServerId;
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

    public List<Accompaniment> getAccompanimentList() {
        return accompanimentList;
    }

    public void setAccompanimentList(List<Accompaniment> accompanimentList) {
        this.accompanimentList = accompanimentList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderItemId", getOrderItemId());
            jsonObject.put("orderItemServerId", getOrderItemServerId());
            jsonObject.put("quantity", getQuantity());
            jsonObject.put("observations", getObservations());
            jsonObject.put("value", getValue());
            jsonObject.put("foodId", getFoodId());
            jsonObject.put("orderId", getOrderId());
            jsonObject.put("productType", getProductType());
            jsonObject.put("accompaniments", getAccompanimentsString());
            jsonObject.put("status", getStatus());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAccompanimentsString() throws JSONException {
        JSONArray accompanimentsArr = new JSONArray();
        if(accompanimentList != null) {
            for (Accompaniment acc : accompanimentList) {
                JSONObject accompanimentObj = new JSONObject();
                accompanimentObj.put("id", acc.getAccompanimentId());
                accompanimentsArr.put(accompanimentObj);
            }
        }
        return accompanimentsArr.toString();
    }

}
