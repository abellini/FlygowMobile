package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Tablet;

public class RepositoryTablet extends Repository<Tablet> {

    private static final String REPOSITORY_TABLET = "RepositoryTablet";

    public RepositoryTablet(Context ctx) {

        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    @Override
    public long save(Tablet tablet) {
        long id = tablet.getTabletId();

        Tablet tab = findById(id);
        if (tab != null) {
            if (tab.getTabletId() != 0) {
                update(tablet);
            }
        } else {
            id = insert(tablet);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Tablet tablet) {
        ContentValues values = new ContentValues();
        values.put(Tablets.COLUMN_NAME_STATUS_ID, tablet.getStatusId());
        values.put(Tablets.COLUMN_NAME_COIN_ID, tablet.getCoinId());
        values.put(Tablets.COLUMN_NAME_NUMBER, tablet.getNumber());
        values.put(Tablets.COLUMN_NAME_IP, tablet.getIp());
        values.put(Tablets.COLUMN_NAME_PORT, tablet.getPort());
        values.put(Tablets.COLUMN_NAME_SERVER_IP, tablet.getServerIP());
        values.put(Tablets.COLUMN_NAME_SERVER_PORT, tablet.getServerPort());
        values.put(Tablets.COLUMN_NAME_ATTENDANT_ID, tablet.getAttendantId());

        return values;
    }

    @Override
    protected long insert(Tablet tablet) {

        ContentValues values = populateContentValues(tablet);
        long id = db.insert(Tablets.TABLE_NAME, "", values);
        Log.i(REPOSITORY_TABLET, "Insert [" + id + "] Tablet record");
        return id;
    }

    @Override
    protected int update(Tablet tablet) {
        ContentValues values = populateContentValues(tablet);
        String _id = String.valueOf(tablet.getTabletId());
        String where = Tablets.COLUMN_NAME_TABLET_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Tablets.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_TABLET, "Update [" + count + "] Tablet record(s)");
        return count;
    }

    public int removeAll() {
        int count = db.delete(Tablets.TABLE_NAME, null, null);
        Log.i(REPOSITORY_TABLET, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    public int delete(long id) {
        String where = Tablets.COLUMN_NAME_TABLET_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Tablets.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_TABLET, "Delete [" + count + "] Tablet record(s)");
        return count;
    }

    @Override
    public Tablet findById(long id) {
        Cursor c = db.query(true, Tablets.TABLE_NAME, Tablet.columns, Tablets.COLUMN_NAME_TABLET_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            int idxId = c.getColumnIndex(Tablets.COLUMN_NAME_TABLET_ID);
            int idxStatus = c.getColumnIndex(Tablets.COLUMN_NAME_STATUS_ID);
            int idxCoinId =  c.getColumnIndex(Tablets.COLUMN_NAME_COIN_ID);
            int idxNumber =  c.getColumnIndex(Tablets.COLUMN_NAME_NUMBER);
            int idxIp =  c.getColumnIndex(Tablets.COLUMN_NAME_IP);
            int idxPort =  c.getColumnIndex(Tablets.COLUMN_NAME_PORT);
            int idxServerIP =  c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_IP);
            int idxServerPort =  c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_PORT);
            int idxAttendantId =  c.getColumnIndex(Tablets.COLUMN_NAME_ATTENDANT_ID);


            Tablet tablet = new Tablet();
            tablet.setTabletId(c.getLong(idxId));
            tablet.setNumber(c.getInt(idxNumber));
            tablet.setCoinId(c.getInt(idxCoinId));
            tablet.setIp(c.getString(idxIp));
            tablet.setPort(c.getInt(idxPort));
            tablet.setServerIP(c.getString(idxServerIP));
            tablet.setServerPort(c.getInt(idxServerPort));
            tablet.setAttendantId(c.getInt(idxAttendantId));
            tablet.setStatusId(c.getInt(idxStatus));
            return tablet;
        }
        return null;
    }

    public Tablet findFirst() {
        Cursor c = db.query(true, Tablets.TABLE_NAME, Tablet.columns, null, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Tablet tablet = new Tablet();
            tablet.setTabletId(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_TABLET_ID)));
            tablet.setNumber(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_NUMBER)));
            tablet.setCoinId(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_COIN_ID)));
            tablet.setIp(c.getString(c.getColumnIndex(Tablets.COLUMN_NAME_IP)));
            tablet.setPort(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_PORT)));
            tablet.setServerIP(c.getString(c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_IP)));
            tablet.setServerPort(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_PORT)));
            tablet.setAttendantId(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_ATTENDANT_ID)));
            return tablet;
        }
        return null;
    }

    public Tablet findLast() {
        Cursor c = db.query(true, Tablets.TABLE_NAME, Tablet.columns, null, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToLast();
            Tablet tablet = new Tablet();
            tablet.setTabletId(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_TABLET_ID)));
            tablet.setNumber(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_NUMBER)));
            tablet.setCoinId(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_COIN_ID)));
            tablet.setIp(c.getString(c.getColumnIndex(Tablets.COLUMN_NAME_IP)));
            tablet.setPort(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_PORT)));
            tablet.setServerIP(c.getString(c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_IP)));
            tablet.setServerPort(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_PORT)));
            tablet.setAttendantId(c.getInt(c.getColumnIndex(Tablets.COLUMN_NAME_ATTENDANT_ID)));
            return tablet;
        }
        return null;
    }

    @Override
    public Cursor getCursor() {
        return db.query(Tablets.TABLE_NAME, Tablet.columns, null, null, null, null, null, null);
    }

    @Override
    public List<Tablet> listAll() {
        Cursor c = getCursor();
        List<Tablet> tablets = new ArrayList<Tablet>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Tablets.COLUMN_NAME_TABLET_ID);
            int idxStatus = c.getColumnIndex(Tablets.COLUMN_NAME_STATUS_ID);
            int idxCoinId =  c.getColumnIndex(Tablets.COLUMN_NAME_COIN_ID);
            int idxNumber =  c.getColumnIndex(Tablets.COLUMN_NAME_NUMBER);
            int idxip =  c.getColumnIndex(Tablets.COLUMN_NAME_IP);
            int idxport =  c.getColumnIndex(Tablets.COLUMN_NAME_PORT);
            int idxserverIP =  c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_IP);
            int idxserverPort =  c.getColumnIndex(Tablets.COLUMN_NAME_SERVER_PORT);
            int idxAttendantId =  c.getColumnIndex(Tablets.COLUMN_NAME_ATTENDANT_ID);

            do {
                Tablet tablet = new Tablet();
                tablets.add(tablet);
                // recupera os atributos de coin
                tablet.setTabletId(c.getLong(idxId));
                tablet.setStatusId(c.getInt(idxStatus));
                tablet.setNumber(c.getInt(idxNumber));
                tablet.setCoinId(c.getInt(idxCoinId));
                tablet.setIp(c.getString(idxip));
                tablet.setPort(c.getInt(idxport));
                tablet.setServerIP(c.getString(idxserverIP));
                tablet.setServerPort(c.getInt(idxserverPort));
                tablet.setAttendantId(c.getInt(idxAttendantId));
            } while (c.moveToNext());
        }
        return tablets;
    }

    public static abstract class Tablets implements BaseColumns {

        public static final String TABLE_NAME = "Tablet";
        public static final String DEFAULT_SORT_ORDER = "tabletId ASC";
        public static final String COLUMN_NAME_TABLET_ID = "tabletId";
        public static final String COLUMN_NAME_STATUS_ID = "statusId";
        public static final String COLUMN_NAME_COIN_ID = "coinId";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_IP = "ip";
        public static final String COLUMN_NAME_PORT = "port";
        public static final String COLUMN_NAME_SERVER_IP = "serverIP";
        public static final String COLUMN_NAME_SERVER_PORT = "serverPort";
        public static final String COLUMN_NAME_ATTENDANT_ID = "attendantId";
        private Tablets() {}

    }

//    @Override
//    public Cursor query(SQLiteQueryBuilder queryBuilder, String[]  projection, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
//        Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
//        return c;
//    }


}
