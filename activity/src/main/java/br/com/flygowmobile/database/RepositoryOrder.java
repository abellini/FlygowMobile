package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.Order;

public class RepositoryOrder extends Repository<Order> {

    @Override
    public long save(Order order) {
        //long id = order.getTabletId();

        //Order o = findById(id);
        // if (o != null) {
        //if (o.getTabletId() != 0) {
        //       update(order);
        // }
        //} else {
        //  id = insert(order);
        //}
        //return id;
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(Order obj) {
        return null;
    }

    @Override
    protected long insert(Order obj) {
        return 0;
    }

    @Override
    protected int update(Order obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Order findById(long id) {
        return null;
    }

    @Override
    public List<Order> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
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
