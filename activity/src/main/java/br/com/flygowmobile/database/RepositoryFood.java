package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.Food;

public class RepositoryFood extends Repository<Food> {

    private static final String REPOSITORY_FOOD = "RepositoryFood";

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
    protected long insert(Food obj) {
        return 0;
    }

    @Override
    protected int update(Food obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Food findById(long id) {
        return null;
    }

    @Override
    public List<Food> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }
}
