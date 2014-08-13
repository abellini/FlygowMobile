package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.flygowmobile.entity.Order;

public class RepositoryOrder extends Repository<Order> {

    private static final String REPOSITORY_ORDER = "RepositoryOrder";

    @Override
    public long save(Order order) {
        long id = order.getOrderId();

        Order o = findById(id);
        if (o != null) {
            if (o.getTabletId() != 0) {
                update(order);
            }
        } else {
            id = insert(order);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Order order) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put(Orders.COLUMN_NAME_ORDER_ID, order.getOrderId());
        values.put(Orders.COLUMN_NAME_ATTENDANT_ID, order.getAttendantId());
        values.put(Orders.COLUMN_NAME_CLIENT_ID, order.getClientId());
        values.put(Orders.COLUMN_NAME_ORDER_HOUR, fm.format(order.getHour()));
        values.put(Orders.COLUMN_NAME_TABLET_ID, order.getTabletId());
        values.put(Orders.COLUMN_NAME_TOTAL_VALUE, order.getTotalValue());

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

    @Override
    public Order findById(long id) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Cursor c = db.query(true, Orders.TABLE_NAME, Order.columns, Orders.COLUMN_NAME_ORDER_ID + "=" + id, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                Order order = new Order();
                order.setOrderId(c.getLong(0));
                order.setClientId(c.getInt(1));
                order.setTotalValue(c.getDouble(2));
                order.setHour(fm.parse(c.getString(3)));
                order.setAttendantId(c.getInt(4));
                return order;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ORDER, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    @Override
    public List<Order> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return db.query(Orders.TABLE_NAME, Order.columns, null, null, null, null, null, null);
    }

    public static abstract class Orders implements BaseColumns {

        public static final String TABLE_NAME = "OrderTab";

        public static final String COLUMN_NAME_ORDER_ID = "orderId";
        public static final String COLUMN_NAME_CLIENT_ID = "clientId";
        public static final String COLUMN_NAME_TABLET_ID = "tabletId";
        public static final String COLUMN_NAME_TOTAL_VALUE = "totalValue";
        public static final String COLUMN_NAME_ORDER_HOUR = "orderHour";
        public static final String COLUMN_NAME_ATTENDANT_ID = "attendantId";

    }
}
