package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Accompaniment;

public class RepositoryAccompaniment extends Repository<Accompaniment> {

    private static final String REPOSITORY_ACCOMPANIMENT = "RepositoryAccompaniment";

    public RepositoryAccompaniment(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Accompaniment accompaniment) {
        long id = accompaniment.getAccompanimentId();

        Accompaniment c = findById(id);
        if (c != null) {
            if (c.getAccompanimentId() != 0) {
                this.update(accompaniment);
            }
        } else {
            id = this.insert(accompaniment);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Accompaniment accompaniment) {

        ContentValues values = new ContentValues();
        values.put(Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID, accompaniment.getAccompanimentId());
        values.put(Accompaniments.COLUMN_NAME_NAME, accompaniment.getName());
        values.put(Accompaniments.COLUMN_NAME_VALUE, accompaniment.getValue());
        values.put(Accompaniments.COLUMN_NAME_DESCRIPTION, accompaniment.getDescription());
        values.put(Accompaniments.COLUMN_NAME_IS_ACTIVE, accompaniment.isActive());
        values.put(Accompaniments.COLUMN_NAME_CATEGORY_ID, accompaniment.getCategoryId());

        return values;
    }

    @Override
    protected long insert(Accompaniment accompaniment) {
        ContentValues values = populateContentValues(accompaniment);
        long id = db.insert(Accompaniments.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ACCOMPANIMENT, "Insert [" + id + "] Accompaniment record");
        return id;
    }

    public int removeAll() {
        int count = db.delete(Accompaniments.TABLE_NAME, null, null);
        Log.i(REPOSITORY_ACCOMPANIMENT, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    protected int update(Accompaniment accompaniment) {
        ContentValues values = populateContentValues(accompaniment);
        String _id = String.valueOf(accompaniment.getAccompanimentId());
        String where = Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Accompaniments.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_ACCOMPANIMENT, "Update [" + count + "] Accompaniment record(s)");
        return count;
    }

    @Override
    public int delete(long id) {
        String where = Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Accompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ACCOMPANIMENT, "Delete [" + count + "] Accompaniment record(s)");
        return count;
    }

    @Override
    public Accompaniment findById(long id) {
        Cursor c = db.query(true, Accompaniments.TABLE_NAME, Accompaniment.columns, Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Accompaniment accompaniment = new Accompaniment(c.getInt(0), c.getString(1), c.getDouble(2), c.getString(3), Boolean.parseBoolean(c.getString(4)), c.getInt(4));
            return accompaniment;
        }
        return null;
    }

    @Override
    public List<Accompaniment> listAll() {

        Cursor c = getCursor();
        List<Accompaniment> accompaniments = new ArrayList<Accompaniment>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID);
            int idxName = c.getColumnIndex(Accompaniments.COLUMN_NAME_NAME);
            int idxValue =  c.getColumnIndex(Accompaniments.COLUMN_NAME_VALUE);
            int idxDescription =  c.getColumnIndex(Accompaniments.COLUMN_NAME_DESCRIPTION);
            int idxIsActive =  c.getColumnIndex(Accompaniments.COLUMN_NAME_IS_ACTIVE);
            int idxCategory =  c.getColumnIndex(Accompaniments.COLUMN_NAME_CATEGORY_ID);

            do {
                Accompaniment accompaniment = new Accompaniment();
                accompaniments.add(accompaniment);
                // recupera os atributos de accompaniment
                accompaniment.setAccompanimentId(c.getInt(idxId));
                accompaniment.setName(c.getString(idxName));
                accompaniment.setValue(c.getDouble(idxValue));
                accompaniment.setDescription(c.getString(idxDescription));
                accompaniment.setActive(Boolean.parseBoolean(c.getString(idxIsActive)));
                accompaniment.setCategoryId(c.getInt(idxCategory));
            } while (c.moveToNext());
        }
        return accompaniments;
    }

    @Override
    public Cursor getCursor() {
        return db.query(Accompaniments.TABLE_NAME, Accompaniment.columns, null, null, null, null, null, null);
    }

    public static abstract class Accompaniments implements BaseColumns {

        public static final String TABLE_NAME = "Accompaniment";

        public static final String COLUMN_NAME_ACCOMPANIMENT_ID = "accompanimentId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IS_ACTIVE = "isActive";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";

    }



}
