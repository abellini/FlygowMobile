package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

import br.com.flygowmobile.entity.FoodAccompaniment;


public class RepositoryFoodAccompaniment extends Repository<FoodAccompaniment> {

    private static final String REPOSITORY_FOOD_ACCOMPANIMENT = "RepositoryFoodAccompaniment";


    public RepositoryFoodAccompaniment(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(FoodAccompaniment foodAccompaniment) {
        return this.insert(foodAccompaniment);
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
        String[] whereArgs = new String[] { _id };
        int count = db.delete(FoodAccompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Delete [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    public int deleteByAccompanimentId(long id) {
        String where = FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(FoodAccompaniments.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD_ACCOMPANIMENT, "Delete [" + count + "] FoodAccompaniments record(s)");
        return count;
    }

    @Override
    protected int update(FoodAccompaniment obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }


    @Override
    public FoodAccompaniment findById(long id) {
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
