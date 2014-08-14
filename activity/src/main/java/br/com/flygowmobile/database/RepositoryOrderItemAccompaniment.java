package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.FoodAccompaniment;
import br.com.flygowmobile.entity.OrderItemAccompaniment;


public class RepositoryOrderItemAccompaniment extends Repository<OrderItemAccompaniment> {

    private static final String REPOSITORY_ORDER_ITEM_ACCOMPANIMENT = "RepositoryOrderItemAccompaniment";
    private RepositoryAccompaniment repositoryAccompaniment;

    public RepositoryOrderItemAccompaniment(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
        repositoryAccompaniment = new RepositoryAccompaniment(ctx);
    }

    @Override
    public long save(OrderItemAccompaniment orderItemAccompaniment) {
        long id = -1;
        long orderItemId = orderItemAccompaniment.getOrderItemId();
        long accompanimentId = orderItemAccompaniment.getAccompanimentId();
        OrderItemAccompaniment orderItemAcc = findById(orderItemId, accompanimentId);
        if (orderItemAcc == null) {
            id = this.insert(orderItemAccompaniment);
        }
        return id;
    }

    public int removeAll() {
        int count = db.delete(OrderItemAccompaniments.TABLE_NAME, null, null);
        Log.i(REPOSITORY_ORDER_ITEM_ACCOMPANIMENT, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    protected ContentValues populateContentValues(OrderItemAccompaniment orderItemAccompaniment) {
        ContentValues values = new ContentValues();
        values.put(OrderItemAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID, orderItemAccompaniment.getAccompanimentId());
        values.put(OrderItemAccompaniments.COLUMN_NAME_ORDER_ITEM_ID, orderItemAccompaniment.getOrderItemId());
        return values;
    }

    @Override
    protected long insert(OrderItemAccompaniment orderItemAccompaniment) {
        ContentValues values = populateContentValues(orderItemAccompaniment);
        long id = db.insert(OrderItemAccompaniments.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ORDER_ITEM_ACCOMPANIMENT, "Insert [" + id + "] FoodAccompaniments record");
        return id;
    }

    public int deleteByOrderItemId(long id) {
        String where = OrderItemAccompaniments.COLUMN_NAME_ORDER_ITEM_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(OrderItemAccompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ORDER_ITEM_ACCOMPANIMENT, "Delete [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    public int deleteByAccompanimentId(long id) {
        String where = OrderItemAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(OrderItemAccompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ORDER_ITEM_ACCOMPANIMENT, "Delete [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    public List<Accompaniment> findByOrderItemId(long id){
        Cursor c = db.query(true, OrderItemAccompaniments.TABLE_NAME, OrderItemAccompaniment.columns, OrderItemAccompaniments.COLUMN_NAME_ORDER_ITEM_ID+ "=" + id, null, null, null, null, null);
        List<Accompaniment> accompaniments = new ArrayList<Accompaniment>();
        if (c.moveToFirst()) {
            int idxAccId = c.getColumnIndex(OrderItemAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID);
            do {
                Accompaniment accompaniment = repositoryAccompaniment.findById(c.getInt(idxAccId));
                if (accompaniment != null) {
                    accompaniments.add(accompaniment);
                }
            } while(c.moveToNext());
        }
        return accompaniments;
    }

    @Override
    protected int update(OrderItemAccompaniment orderItemAccompaniment) {

        ContentValues values = populateContentValues(orderItemAccompaniment);
        String _id = String.valueOf(orderItemAccompaniment.getOrderItemId());
        String _accompanimentId = String.valueOf(orderItemAccompaniment.getAccompanimentId());
        String where = OrderItemAccompaniments.COLUMN_NAME_ORDER_ITEM_ID + "=? and " + OrderItemAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String[] whereArgs = new String[]{_id, _accompanimentId};
        int count = db.update(OrderItemAccompaniments.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_ORDER_ITEM_ACCOMPANIMENT, "Update [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public OrderItemAccompaniment findById(long id) {return null;}

    public OrderItemAccompaniment findById(long orderItemId, long accompanimentId) {

        Cursor c = db.query(true, OrderItemAccompaniments.TABLE_NAME, OrderItemAccompaniment.columns, OrderItemAccompaniments.COLUMN_NAME_ORDER_ITEM_ID + "=" + orderItemId + " and " + OrderItemAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=" + accompanimentId, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            OrderItemAccompaniment orderItemAccompaniment = new OrderItemAccompaniment(c.getLong(0), c.getLong(1));
            return orderItemAccompaniment;
        }
        return null;
    }

    @Override
    public List<OrderItemAccompaniment> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return db.query(OrderItemAccompaniments.TABLE_NAME, OrderItemAccompaniment.columns, null, null, null, null, null, null);
    }

    public static abstract class OrderItemAccompaniments implements BaseColumns {

        public static final String TABLE_NAME = "OrderItemAccompaniment";

        public static final String COLUMN_NAME_ORDER_ITEM_ID = "orderItemId";
        public static final String COLUMN_NAME_ACCOMPANIMENT_ID = "accompanimentId";

    }
}
