package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Coin;


public class RepositoryCoin extends Repository<Coin> {

    private static final String REPOSITORY_COIN = "RepositoryCoin";

    public static abstract class Coins implements BaseColumns {

        private Coins() {}

        public static final String TABLE_NAME = "Coin";

        public static final String DEFAULT_SORT_ORDER = "coinId ASC";

        public static final String COLUMN_NAME_COIN_ID = "coinId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SYMBOL = "symbol";
        public static final String COLUMN_NAME_CONVERSION = "conversion";

    }

    public RepositoryCoin(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Coin coin) {
        long id = coin.getCoinId();

        Coin c = findById(id);
        if (c != null) {
            if (c.getCoinId() != 0) {
                this.update(coin);
            }
        } else {
            id = this.insert(coin);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Coin coin) {

        ContentValues values = new ContentValues();
        values.put(Coins.COLUMN_NAME_COIN_ID, coin.getCoinId());
        values.put(Coins.COLUMN_NAME_NAME, coin.getName());
        values.put(Coins.COLUMN_NAME_SYMBOL, coin.getSymbol());
        values.put(Coins.COLUMN_NAME_CONVERSION, coin.getConversion());

        return values;
    }

    @Override
    protected long insert(Coin coin) {

        ContentValues values = populateContentValues(coin);
        long id = db.insert(Coins.TABLE_NAME, "", values);
        Log.i(REPOSITORY_COIN, "Insert [" + id + "] record");
        return id;
    }

    @Override
    protected int update(Coin coin) {

        ContentValues values = populateContentValues(coin);
        String _id = String.valueOf(coin.getCoinId());
        String where = Coins.COLUMN_NAME_COIN_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Coins.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_COIN, "Update [" + count + "] record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = Coins.COLUMN_NAME_COIN_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Coins.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_COIN, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    public Coin findById(long id) {

        Cursor c = db.query(true, Coins.TABLE_NAME, Coin.columns, Coins.COLUMN_NAME_COIN_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Coin coin = new Coin(c.getLong(0), c.getString(1), c.getString(2), c.getDouble(3));
            return coin;
        }
        return null;
    }

    @Override
    public List<Coin> listAll() {
        Cursor c = getCursor();
        List<Coin> coins = new ArrayList<Coin>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Coins.COLUMN_NAME_COIN_ID);
            int idxName = c.getColumnIndex(Coins.COLUMN_NAME_NAME);
            int idxSymbol =  c.getColumnIndex(Coins.COLUMN_NAME_SYMBOL);
            int idxConversion =  c.getColumnIndex(Coins.COLUMN_NAME_CONVERSION);

            do {
                Coin coin = new Coin();
                coins.add(coin);
                // recupera os atributos de coin
                coin.setCoinId(c.getLong(idxId));
                coin.setName(c.getString(idxName));
                coin.setSymbol(c.getString(idxSymbol));
                coin.setConversion(c.getDouble(idxConversion));
            } while (c.moveToNext());
        }
        return coins;
    }

    @Override
    public Cursor getCursor() {

        return db.query(Coins.TABLE_NAME, Coin.columns, null, null, null, null, null, null);
    }
}
