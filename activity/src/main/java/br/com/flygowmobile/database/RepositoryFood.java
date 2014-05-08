package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.Food;

public class RepositoryFood extends Repository<Food> {

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

    @Override
    public long save(Food obj) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(Food obj) {
        return null;
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
