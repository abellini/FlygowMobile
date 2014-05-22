package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.FoodAccompaniment;


public class RepositoryFoodAccompaniment extends Repository<FoodAccompaniment> {

    private static final String REPOSITORY_FOOD_ACCOMPANIMENT = "RepositoryFoodAccompaniment";


    public RepositoryFoodAccompaniment(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(FoodAccompaniment foodAccompaniment) {
        long id = foodAccompaniment.getFoodId();

        FoodAccompaniment foodAcc = findById(id);
        if (foodAcc != null) {
            if (foodAcc.getFoodId() != 0) {
                this.update(foodAccompaniment);
            }
        } else {
            id = this.insert(foodAccompaniment);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(FoodAccompaniment foodAccompaniment) {
        return null;
    }

    @Override
    protected long insert(FoodAccompaniment foodAccompaniment) {
        return 0;
    }

    @Override
    protected int update(FoodAccompaniment foodAccompaniment) {
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
