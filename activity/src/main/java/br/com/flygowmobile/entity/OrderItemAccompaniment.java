package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryFoodAccompaniment;
import br.com.flygowmobile.database.RepositoryOrderItemAccompaniment;

public class OrderItemAccompaniment implements Serializable {

    public static String[] columns = new String[]{
            RepositoryOrderItemAccompaniment.OrderItemAccompaniments.COLUMN_NAME_ORDER_ITEM_ID,
            RepositoryOrderItemAccompaniment.OrderItemAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID
    };

    private long accompanimentId;
    private long orderItemId;

    public OrderItemAccompaniment() {
    }

    public OrderItemAccompaniment(long accompanimentId, long orderItemId) {
        this.accompanimentId = accompanimentId;
        this.orderItemId = orderItemId;
    }

    public long getAccompanimentId() {
        return accompanimentId;
    }

    public void setAccompanimentId(long accompanimentId) {
        this.accompanimentId = accompanimentId;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }
}
