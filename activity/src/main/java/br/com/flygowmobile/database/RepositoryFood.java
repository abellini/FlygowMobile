package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Food;

public class RepositoryFood extends Repository<Food> {

    private static final String REPOSITORY_FOOD = "RepositoryFood";

    public RepositoryFood(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Food food) {

        long id = food.getFoodId();

        Food f = findById(id);
        if (f != null) {
            if (f.getFoodId() != 0) {
                this.update(food);
            }
        } else {
            id = this.insert(food);
        }
        return id;
    }

    public int removeAll() {
        int count = db.delete(Foods.TABLE_NAME, null, null);
        Log.i(REPOSITORY_FOOD, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    protected ContentValues populateContentValues(Food food) {

        ContentValues values = new ContentValues();
        values.put(Foods.COLUMN_NAME_FOOD_ID, food.getFoodId());
        values.put(Foods.COLUMN_NAME_NAME, food.getName());
        values.put(Foods.COLUMN_NAME_VALUE, food.getValue());
        values.put(Foods.COLUMN_NAME_DESCRIPTION, food.getDescription());
        values.put(Foods.COLUMN_NAME_NUTRITIONAL_INFO, food.getNutritionalInfo());
        values.put(Foods.COLUMN_NAME_IS_ACTIVE, food.isActive());
        values.put(Foods.COLUMN_NAME_CATEGORY_ID, food.getCategoryId());

        return values;
    }

    @Override
    protected long insert(Food food) {

        ContentValues values = populateContentValues(food);
        long id = db.insert(Foods.TABLE_NAME, "", values);
        Log.i(REPOSITORY_FOOD, "Insert [" + id + "] Food record");
        return id;
    }

    @Override
    protected int update(Food food) {

        ContentValues values = populateContentValues(food);
        String _id = String.valueOf(food.getFoodId());
        String where = Foods.COLUMN_NAME_FOOD_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Foods.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_FOOD, "Update [" + count + "] Food record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = Foods.COLUMN_NAME_FOOD_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Foods.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_FOOD, "Delete [" + count + "] Food record(s)");
        return count;
    }

    @Override
    public Food findById(long id) {

        Cursor c = db.query(true, Foods.TABLE_NAME, Food.columns, Foods.COLUMN_NAME_FOOD_ID+ "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Food food = new Food(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4), Boolean.parseBoolean(c.getString(5)), c.getInt(5));
            return food;
        }
        return null;
    }

    @Override
    public List<Food> listAll() {

        Cursor c = getCursor();
        List<Food> foods = new ArrayList<Food>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Foods.COLUMN_NAME_FOOD_ID);
            int idxName = c.getColumnIndex(Foods.COLUMN_NAME_NAME);
            int idxValue =  c.getColumnIndex(Foods.COLUMN_NAME_VALUE);
            int idxDescription =  c.getColumnIndex(Foods.COLUMN_NAME_DESCRIPTION);
            int idxNutrionalInfo =  c.getColumnIndex(Foods.COLUMN_NAME_NUTRITIONAL_INFO);
            int idxIsActive =  c.getColumnIndex(Foods.COLUMN_NAME_IS_ACTIVE);
            int idxCategory =  c.getColumnIndex(Foods.COLUMN_NAME_CATEGORY_ID);

            do {
                Food food = new Food();
                foods.add(food);
                // recupera os atributos de food
                food.setFoodId(c.getInt(idxId));
                food.setName(c.getString(idxName));
                food.setValue(c.getDouble(idxValue));
                food.setDescription(c.getString(idxDescription));
                food.setNutritionalInfo(c.getString(idxNutrionalInfo));
                food.setActive(Boolean.parseBoolean(c.getString(idxIsActive)));
                food.setCategoryId(c.getInt(idxCategory));
            } while (c.moveToNext());
        }
        return foods;
    }

    @Override
    public Cursor getCursor() {

        return db.query(Foods.TABLE_NAME, Food.columns, null, null, null, null, null, null);
    }

    public static abstract class Foods implements BaseColumns {

        public static final String TABLE_NAME = "Food";

        public static final String COLUMN_NAME_FOOD_ID = "foodId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_VIDEO = "video";
        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_OPERATION_AREA_ID = "operationAreaId";
        public static final String COLUMN_NAME_NUTRITIONAL_INFO = "nutritionalInfo";
        public static final String COLUMN_NAME_IS_ACTIVE = "isActive";

    }
}
