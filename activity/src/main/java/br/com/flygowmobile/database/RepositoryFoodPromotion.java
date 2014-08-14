package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.FoodPromotion;


public class RepositoryFoodPromotion extends Repository<FoodPromotion> {

    private static final String REPOSITORY_FOOD_PROMOTION = "RepositoryFoodPromotion";
    private RepositoryFood repositoryFood;

    public RepositoryFoodPromotion(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
        repositoryFood = new RepositoryFood(ctx);
    }

    @Override
    public long save(FoodPromotion foodPromotion) {

        long id = -1;
        long promotionId = foodPromotion.getPromotionId();
        long foodId = foodPromotion.getFoodId();
        FoodPromotion f = findById(promotionId, foodId);
        if (f == null) {
            id = this.insert(foodPromotion);
        }
        return id;
    }

    public int removeAll() {
        int count = db.delete(FoodPromotions.TABLE_NAME, null, null);
        Log.i(REPOSITORY_FOOD_PROMOTION, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    protected ContentValues populateContentValues(FoodPromotion foodPromotion) {
        ContentValues values = new ContentValues();
        values.put(FoodPromotions.COLUMN_NAME_PROMOTION_ID, foodPromotion.getPromotionId());
        values.put(FoodPromotions.COLUMN_NAME_FOOD_ID, foodPromotion.getFoodId());
        return values;
    }

    @Override
    protected long insert(FoodPromotion foodPromotion) {
        ContentValues values = populateContentValues(foodPromotion);
        long id = db.insert(FoodPromotions.TABLE_NAME, "", values);
        Log.i(REPOSITORY_FOOD_PROMOTION, "Insert [" + id + "] FoodPromotions record");
        return id;
    }

    public List<Food> listByPromotion(long promoId) {
        Cursor c = db.query(true, FoodPromotions.TABLE_NAME, FoodPromotion.columns, FoodPromotions.COLUMN_NAME_PROMOTION_ID + "=" + promoId, null, null, null, null, null);
        List<Food> foods = new ArrayList<Food>();
        if (c.moveToFirst()) {
            int idxFoodId = c.getColumnIndex(FoodPromotions.COLUMN_NAME_FOOD_ID);
            do {
                Food food = repositoryFood.findById(c.getInt(idxFoodId));
                if (food != null) {
                    foods.add(food);
                }
            } while (c.moveToNext());
        }
        return foods;
    }

    public int deleteByFoodId(long id) {
        String where = FoodPromotions.COLUMN_NAME_FOOD_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(FoodPromotions.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD_PROMOTION, "Delete [" + count + "] FoodPromotions record(s)");
        return count;
    }

    public int deleteByPromotionId(long id) {
        String where = FoodPromotions.COLUMN_NAME_PROMOTION_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[]{_id};
        int count = db.delete(FoodPromotions.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD_PROMOTION, "Delete [" + count + "] FoodPromotions record(s)");
        return count;
    }

    @Override
    protected int update(FoodPromotion foodPromotion) {

        ContentValues values = populateContentValues(foodPromotion);
        String _id = String.valueOf(foodPromotion.getFoodId());
        String _promotionId = String.valueOf(foodPromotion.getPromotionId());
        String where = FoodPromotions.COLUMN_NAME_FOOD_ID + "=? and " + FoodPromotions.COLUMN_NAME_PROMOTION_ID + "=?";
        String[] whereArgs = new String[]{_id, _promotionId};
        int count = db.update(FoodPromotions.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_FOOD_PROMOTION, "Update [" + count + "] FoodPromotions record(s)");
        return count;

    }

    @Override
    public int delete(long id) {
        return 0;
    }

    public FoodPromotion findById(long promotionId, long foodId) {

        Cursor c = db.query(true, FoodPromotions.TABLE_NAME, FoodPromotion.columns, FoodPromotions.COLUMN_NAME_PROMOTION_ID + "=" + promotionId + " and " + FoodPromotions.COLUMN_NAME_FOOD_ID + "=" + foodId, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            FoodPromotion foodPromotion = new FoodPromotion(c.getLong(0), c.getLong(1));
            return foodPromotion;
        }
        return null;
    }


    @Override
    public FoodPromotion findById(long id) {

        Cursor c = db.query(true, FoodPromotions.TABLE_NAME, FoodPromotion.columns, FoodPromotions.COLUMN_NAME_FOOD_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            FoodPromotion foodPromotion = new FoodPromotion(c.getLong(0), c.getLong(1));
            return foodPromotion;
        }
        return null;
    }

    @Override
    public List<FoodPromotion> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return db.query(FoodPromotions.TABLE_NAME, FoodPromotion.columns, null, null, null, null, null, null);
    }

    public static abstract class FoodPromotions implements BaseColumns {

        public static final String TABLE_NAME = "FoodPromotion";

        public static final String COLUMN_NAME_FOOD_ID = "foodId";
        public static final String COLUMN_NAME_PROMOTION_ID = "promotionId";

    }
}
