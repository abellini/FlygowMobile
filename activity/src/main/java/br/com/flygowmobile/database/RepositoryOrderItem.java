package br.com.flygowmobile.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.OrderItem;

public class RepositoryOrderItem extends Repository<OrderItem> {

    public static abstract class OrderItems implements BaseColumns {

        public static final String TABLE_NAME = "OrderItem";

        public static final String COLUMN_NAME_ORDER_ITEM_ID = "orderItemId";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_OBSERVATIONS = "observations";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_FOOD_ID = "foodId";
        public static final String COLUMN_NAME_ORDER_ID = "orderId";

    }

    @Override
    public long save(OrderItem obj) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(OrderItem obj) {
        return null;
    }

    @Override
    protected long insert(OrderItem obj) {
        return 0;
    }

    @Override
    protected int update(OrderItem obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public OrderItem findById(long id) {
        return null;
    }

    @Override
    public List<OrderItem> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }
}
