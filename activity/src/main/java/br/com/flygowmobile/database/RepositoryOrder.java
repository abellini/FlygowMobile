package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Order;

public class RepositoryOrder extends Repository<Order> {

    private static final String REPOSITORY_ORDER = "RepositoryOrder";

    public RepositoryOrder(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Order order) {
        long id = order.getOrderId();

        Order o = findById(id);
        if (o != null) {
            update(order);
        } else {
            id = insert(order);
        }
        return id;
    }


    @Override
    protected ContentValues populateContentValues(Order order) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues values = new ContentValues();
        values.put(Orders.COLUMN_NAME_ATTENDANT_ID, order.getAttendantId());
        values.put(Orders.COLUMN_NAME_CLIENT_ID, order.getClientId());
        values.put(Orders.COLUMN_NAME_ORDER_HOUR, fm.format(order.getHour()));
        values.put(Orders.COLUMN_NAME_TABLET_ID, order.getTabletId());
        values.put(Orders.COLUMN_NAME_TOTAL_VALUE, order.getTotalValue());
        values.put(Orders.COLUMN_NAME_STATUS_TYPE, order.getStatusType());

        return values;
    }

    @Override
    protected long insert(Order order) {
        ContentValues values = populateContentValues(order);
        long id = db.insert(RepositoryOrder.Orders.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ORDER, "Insert [" + id + "] Order record");
        return id;
    }

    @Override
    protected int update(Order order) {
        ContentValues values = populateContentValues(order);
        String _id = String.valueOf(order.getOrderId());
        String where = Orders.COLUMN_NAME_ORDER_ID + "=?";
        String[] whereArgs = new String[]{_id};
        int count = db.update(Orders.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_ORDER, "Update [" + count + "] Order record(s)");
        return count;
    }

    @Override
    public int delete(long id) {
        String where = Orders.COLUMN_NAME_ORDER_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(Orders.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ORDER, "Delete [" + count + "] Order record(s)");
        return count;
    }

    public int removeAll() {
        int count = db.delete(Orders.TABLE_NAME, null, null);
        Log.i(REPOSITORY_ORDER, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    public Order findById(long id) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Cursor c = db.query(true, Orders.TABLE_NAME, Order.columns, Orders.COLUMN_NAME_ORDER_ID + "=" + id, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                int idxId = c.getColumnIndex(Orders.COLUMN_NAME_ORDER_ID);
                int idxAttendantId = c.getColumnIndex(Orders.COLUMN_NAME_ATTENDANT_ID);
                int idxClientId = c.getColumnIndex(Orders.COLUMN_NAME_CLIENT_ID);
                int idxOrderHour = c.getColumnIndex(Orders.COLUMN_NAME_ORDER_HOUR);
                int idxTabletId = c.getColumnIndex(Orders.COLUMN_NAME_TABLET_ID);
                int idxTotalValue = c.getColumnIndex(Orders.COLUMN_NAME_TOTAL_VALUE);
                int idxStatusType = c.getColumnIndex(Orders.COLUMN_NAME_STATUS_TYPE);

                Order order = new Order();
                order.setOrderId(c.getLong(idxId));
                order.setClientId(c.getInt(idxClientId));
                order.setTotalValue(c.getDouble(idxTotalValue));
                order.setHour(fm.parse(c.getString(idxOrderHour)));
                order.setAttendantId(c.getInt(idxAttendantId));
                order.setTabletId(c.getLong(idxTabletId));
                order.setStatusType(c.getInt(idxStatusType));
                return order;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ORDER, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    public Order getByStatusType(int statusType) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Cursor c = db.query(true, Orders.TABLE_NAME, Order.columns, Orders.COLUMN_NAME_STATUS_TYPE + "=" + statusType, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                int idxId = c.getColumnIndex(Orders.COLUMN_NAME_ORDER_ID);
                int idxAttendantId = c.getColumnIndex(Orders.COLUMN_NAME_ATTENDANT_ID);
                int idxClientId = c.getColumnIndex(Orders.COLUMN_NAME_CLIENT_ID);
                int idxOrderHour = c.getColumnIndex(Orders.COLUMN_NAME_ORDER_HOUR);
                int idxTabletId = c.getColumnIndex(Orders.COLUMN_NAME_TABLET_ID);
                int idxTotalValue = c.getColumnIndex(Orders.COLUMN_NAME_TOTAL_VALUE);
                int idxStatusType = c.getColumnIndex(Orders.COLUMN_NAME_STATUS_TYPE);

                Order order = new Order();
                order.setOrderId(c.getLong(idxId));
                order.setClientId(c.getInt(idxClientId));
                order.setTotalValue(c.getDouble(idxTotalValue));
                order.setHour(fm.parse(c.getString(idxOrderHour)));
                order.setAttendantId(c.getInt(idxAttendantId));
                order.setTabletId(c.getLong(idxTabletId));
                order.setStatusType(c.getInt(idxStatusType));
                return order;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ORDER, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    @Override
    public List<Order> listAll() {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Cursor c = getCursor();
        List<Order> orders = new ArrayList<Order>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Orders.COLUMN_NAME_ORDER_ID);
            int idxAttendantId = c.getColumnIndex(Orders.COLUMN_NAME_ATTENDANT_ID);
            int idxClientId = c.getColumnIndex(Orders.COLUMN_NAME_CLIENT_ID);
            int idxOrderHour = c.getColumnIndex(Orders.COLUMN_NAME_ORDER_HOUR);
            int idxTabletId = c.getColumnIndex(Orders.COLUMN_NAME_TABLET_ID);
            int idxTotalValue = c.getColumnIndex(Orders.COLUMN_NAME_TOTAL_VALUE);
            int idxStatusType = c.getColumnIndex(Orders.COLUMN_NAME_STATUS_TYPE);

            do {
                Order order = new Order();
                orders.add(order);
                order.setOrderId(c.getLong(idxId));
                order.setClientId(c.getInt(idxClientId));
                order.setTotalValue(c.getDouble(idxTotalValue));
                try {
                    order.setHour(fm.parse(c.getString(idxOrderHour)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                order.setAttendantId(c.getInt(idxAttendantId));
                order.setTabletId(c.getLong(idxTabletId));
                order.setStatusType(c.getInt(idxStatusType));
            } while (c.moveToNext());
        }
        return orders;
    }

    @Override
    public Cursor getCursor() {
        return db.query(Orders.TABLE_NAME, Order.columns, null, null, null, null, null, null);
    }

    public static abstract class Orders implements BaseColumns {

        public static final String TABLE_NAME = "OrderTab";

        public static final String COLUMN_NAME_ORDER_ID = "orderId";
        public static final String COLUMN_NAME_ORDER_SERVER_ID = "orderServerId";
        public static final String COLUMN_NAME_CLIENT_ID = "clientId";
        public static final String COLUMN_NAME_TABLET_ID = "tabletId";
        public static final String COLUMN_NAME_TOTAL_VALUE = "totalValue";
        public static final String COLUMN_NAME_ORDER_HOUR = "orderHour";
        public static final String COLUMN_NAME_ATTENDANT_ID = "attendantId";
        public static final String COLUMN_NAME_STATUS_TYPE = "statusType";

    }
}
