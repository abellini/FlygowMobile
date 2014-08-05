package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Accompaniment;
import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.FoodAccompaniment;
import br.com.flygowmobile.entity.FoodPromotion;


public class RepositoryFoodAccompaniment extends Repository<FoodAccompaniment> {

    private static final String REPOSITORY_FOOD_ACCOMPANIMENT = "RepositoryFoodAccompaniment";
    private RepositoryAccompaniment repositoryAccompaniment;

    public RepositoryFoodAccompaniment(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
        repositoryAccompaniment = new RepositoryAccompaniment(ctx);
    }

    @Override
    public long save(FoodAccompaniment foodAccompaniment) {
        long id = 0;
        long foodId = foodAccompaniment.getFoodId();
        long accompanimentId = foodAccompaniment.getAccompanimentId();
        FoodAccompaniment f = findById(foodId, accompanimentId);
        if (f != null) {
            if (f.getFoodId() != 0) {
                this.update(foodAccompaniment);
            }
        } else {
            id = this.insert(foodAccompaniment);
        }
        return id;
    }

    public int removeAll() {
        int count = db.delete(FoodAccompaniments.TABLE_NAME, null, null);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    protected ContentValues populateContentValues(FoodAccompaniment foodAccompaniment) {
        ContentValues values = new ContentValues();
        values.put(FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID, foodAccompaniment.getAccompanimentId());
        values.put(FoodAccompaniments.COLUMN_NAME_FOOD_ID, foodAccompaniment.getFoodId());
        return values;
    }

    @Override
    protected long insert(FoodAccompaniment foodAccompaniment) {
        ContentValues values = populateContentValues(foodAccompaniment);
        long id = db.insert(FoodAccompaniments.TABLE_NAME, "", values);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Insert [" + id + "] FoodAccompaniments record");
        return id;
    }

    public int deleteByFoodId(long id) {
        String where = FoodAccompaniments.COLUMN_NAME_FOOD_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(FoodAccompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Delete [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    public int deleteByAccompanimentId(long id) {
        String where = FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(FoodAccompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Delete [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    public List<Accompaniment> findByFoodId(long id){
        Cursor c = db.query(true, FoodAccompaniments.TABLE_NAME, FoodAccompaniment.columns, FoodAccompaniments.COLUMN_NAME_FOOD_ID+ "=" + id, null, null, null, null, null);
        List<Accompaniment> accompaniments = new ArrayList<Accompaniment>();
        if (c.moveToFirst()) {
            int idxAccId = c.getColumnIndex(FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID);
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
    protected int update(FoodAccompaniment foodAccompaniment) {

        ContentValues values = populateContentValues(foodAccompaniment);
        String _id = String.valueOf(foodAccompaniment.getFoodId());
        String _accompanimentId = String.valueOf(foodAccompaniment.getAccompanimentId());
        String where = FoodAccompaniments.COLUMN_NAME_FOOD_ID + "=? and " + FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String[] whereArgs = new String[]{_id, _accompanimentId};
        int count = db.update(FoodAccompaniments.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Update [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    @Override
    public int delete(long id) {
        return 0;
    }


    @Override
    public FoodAccompaniment findById(long id) {

        Cursor c = db.query(true, FoodAccompaniments.TABLE_NAME, FoodAccompaniment.columns, FoodAccompaniments.COLUMN_NAME_FOOD_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            FoodAccompaniment foodAccompaniment = new FoodAccompaniment(c.getLong(0), c.getLong(1));
            return foodAccompaniment;
        }
        return null;
    }

    public FoodAccompaniment findById(long foodId, long accompanimentId) {

        Cursor c = db.query(true, FoodAccompaniments.TABLE_NAME, FoodAccompaniment.columns, FoodAccompaniments.COLUMN_NAME_FOOD_ID + "=" + foodId + " and " + FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=" + accompanimentId, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            FoodAccompaniment foodAccompaniment = new FoodAccompaniment(c.getLong(0), c.getLong(1));
            return foodAccompaniment;
        }
        return null;
    }

    @Override
    public List<FoodAccompaniment> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return db.query(FoodAccompaniments.TABLE_NAME, FoodAccompaniment.columns, null, null, null, null, null, null);
    }

    public static abstract class FoodAccompaniments implements BaseColumns {

        public static final String TABLE_NAME = "FoodAccompaniment";

        public static final String COLUMN_NAME_FOOD_ID = "foodId";
        public static final String COLUMN_NAME_ACCOMPANIMENT_ID = "accompanimentId";

    }
}
