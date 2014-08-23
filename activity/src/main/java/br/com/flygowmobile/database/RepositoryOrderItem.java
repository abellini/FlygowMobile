package br.com.flygowmobile.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.OrderItem;

public class RepositoryOrderItem extends Repository<OrderItem> {

    private static final String REPOSITORY_ORDER_ITEM = "RepositoryOrderItem";

    public RepositoryOrderItem(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(OrderItem orderItem) {
        long id = orderItem.getFoodId();

        // Pesquisa por Food porque o id do Item ainda não existe
        OrderItem o = findByFood(id);
        if (o != null) {
            update(orderItem);
        } else {
            id = insert(orderItem);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(OrderItem orderItem) {
        ContentValues values = new ContentValues();
        values.put(OrderItems.COLUMN_NAME_ORDER_ID, orderItem.getOrderId());
        values.put(OrderItems.COLUMN_NAME_FOOD_ID, orderItem.getFoodId());
        values.put(OrderItems.COLUMN_NAME_OBSERVATIONS, orderItem.getObservations());
        values.put(OrderItems.COLUMN_NAME_QUANTITY, orderItem.getQuantity());
        values.put(OrderItems.COLUMN_NAME_VALUE, orderItem.getValue());
        values.put(OrderItems.COLUMN_NAME_PRODUCT_TYPE, orderItem.getProductType());
        return values;
    }

    @Override
    protected long insert(OrderItem orderItem) {
        ContentValues values = populateContentValues(orderItem);
        long id = db.insert(RepositoryOrderItem.OrderItems.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ORDER_ITEM, "Insert [" + id + "] Order Item record");
        return id;

    }

    @Override
    protected int update(OrderItem orderItem) {
        ContentValues values = populateContentValues(orderItem);
        String _id = String.valueOf(orderItem.getOrderItemId());
        String where = OrderItems.COLUMN_NAME_ORDER_ITEM_ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = db.update(OrderItems.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_ORDER_ITEM, "Update [" + count + "] Order Item record(s)");
        return count;

    }

    @Override
    public int delete(long id) {
        String where = OrderItems.COLUMN_NAME_ORDER_ITEM_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(OrderItems.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ORDER_ITEM, "Delete [" + count + "] Order Item record(s)");
        return count;
    }

    public int removeAll() {
        int count = db.delete(OrderItems.TABLE_NAME, null, null);
        Log.i(REPOSITORY_ORDER_ITEM, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    public OrderItem findById(long id) {
        try {
            Cursor c = db.query(true, OrderItems.TABLE_NAME, OrderItem.columns, OrderItems.COLUMN_NAME_ORDER_ITEM_ID + "=" + id, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                int idxId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ITEM_ID);
                int idxFood = c.getColumnIndex(OrderItems.COLUMN_NAME_FOOD_ID);
                int idxObs = c.getColumnIndex(OrderItems.COLUMN_NAME_OBSERVATIONS);
                int idxOrderId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ID);
                int idxQuantity = c.getColumnIndex(OrderItems.COLUMN_NAME_QUANTITY);
                int idxValue = c.getColumnIndex(OrderItems.COLUMN_NAME_VALUE);
                int idxProductType = c.getColumnIndex(OrderItems.COLUMN_NAME_PRODUCT_TYPE);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderItemId(c.getLong(idxId));
                orderItem.setFoodId(c.getLong(idxFood));
                orderItem.setObservations(c.getString(idxObs));
                orderItem.setOrderId(c.getLong(idxOrderId));
                orderItem.setQuantity(c.getInt(idxQuantity));
                orderItem.setValue(c.getDouble(idxValue));
                orderItem.setProductType(c.getString(idxProductType));
                return orderItem;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ORDER_ITEM, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    public OrderItem findByFood(long foodId) {
        try {
            Cursor c = db.query(true, OrderItems.TABLE_NAME, OrderItem.columns, OrderItems.COLUMN_NAME_FOOD_ID + "=" + foodId, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                int idxId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ITEM_ID);
                int idxFood = c.getColumnIndex(OrderItems.COLUMN_NAME_FOOD_ID);
                int idxObs = c.getColumnIndex(OrderItems.COLUMN_NAME_OBSERVATIONS);
                int idxOrderId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ID);
                int idxQuantity = c.getColumnIndex(OrderItems.COLUMN_NAME_QUANTITY);
                int idxValue = c.getColumnIndex(OrderItems.COLUMN_NAME_VALUE);
                int idxProductType = c.getColumnIndex(OrderItems.COLUMN_NAME_PRODUCT_TYPE);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderItemId(c.getLong(idxId));
                orderItem.setFoodId(c.getLong(idxFood));
                orderItem.setObservations(c.getString(idxObs));
                orderItem.setOrderId(c.getLong(idxOrderId));
                orderItem.setQuantity(c.getInt(idxQuantity));
                orderItem.setValue(c.getDouble(idxValue));
                orderItem.setProductType(c.getString(idxProductType));
                return orderItem;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ORDER_ITEM, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    @Override
    public List<OrderItem> listAll() {

        Cursor c = getCursor();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ITEM_ID);
            int idxFood = c.getColumnIndex(OrderItems.COLUMN_NAME_FOOD_ID);
            int idxObs = c.getColumnIndex(OrderItems.COLUMN_NAME_OBSERVATIONS);
            int idxOrderId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ID);
            int idxQuantity = c.getColumnIndex(OrderItems.COLUMN_NAME_QUANTITY);
            int idxValue = c.getColumnIndex(OrderItems.COLUMN_NAME_VALUE);
            int idxProductType = c.getColumnIndex(OrderItems.COLUMN_NAME_PRODUCT_TYPE);

            do {
                OrderItem orderItem = new OrderItem();
                orderItems.add(orderItem);
                orderItem.setOrderItemId(c.getLong(idxId));
                orderItem.setFoodId(c.getLong(idxFood));
                orderItem.setObservations(c.getString(idxObs));
                orderItem.setOrderId(c.getLong(idxOrderId));
                orderItem.setQuantity(c.getInt(idxQuantity));
                orderItem.setValue(c.getDouble(idxValue));
                orderItem.setProductType(c.getString(idxProductType));
            } while (c.moveToNext());
        }
        return orderItems;
    }

    public List<OrderItem> listAllByOrder(long orderId) {
        Cursor c = db.query(true, OrderItems.TABLE_NAME, OrderItem.columns, OrderItems.COLUMN_NAME_ORDER_ID + "=" + orderId, null, null, null, null, null);
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ITEM_ID);
            int idxFood = c.getColumnIndex(OrderItems.COLUMN_NAME_FOOD_ID);
            int idxObs = c.getColumnIndex(OrderItems.COLUMN_NAME_OBSERVATIONS);
            int idxOrderId = c.getColumnIndex(OrderItems.COLUMN_NAME_ORDER_ID);
            int idxQuantity = c.getColumnIndex(OrderItems.COLUMN_NAME_QUANTITY);
            int idxValue = c.getColumnIndex(OrderItems.COLUMN_NAME_VALUE);
            int idxProductType = c.getColumnIndex(OrderItems.COLUMN_NAME_PRODUCT_TYPE);

            do {
                OrderItem orderItem = new OrderItem();
                orderItems.add(orderItem);
                orderItem.setOrderItemId(c.getLong(idxId));
                orderItem.setFoodId(c.getLong(idxFood));
                orderItem.setObservations(c.getString(idxObs));
                orderItem.setOrderId(c.getLong(idxOrderId));
                orderItem.setQuantity(c.getInt(idxQuantity));
                orderItem.setValue(c.getDouble(idxValue));
                orderItem.setProductType(c.getString(idxProductType));
            } while (c.moveToNext());
        }
        return orderItems;
    }

    @Override
    public Cursor getCursor() {
        return db.query(OrderItems.TABLE_NAME, OrderItem.columns, null, null, null, null, null, null);
    }

    public static abstract class OrderItems implements BaseColumns {

        public static final String TABLE_NAME = "OrderItem";

        public static final String COLUMN_NAME_ORDER_ITEM_ID = "orderItemId";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_OBSERVATIONS = "observations";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_FOOD_ID = "foodId";
        public static final String COLUMN_NAME_ORDER_ID = "orderId";
        public static final String COLUMN_NAME_PRODUCT_TYPE = "productType";

    }
}
