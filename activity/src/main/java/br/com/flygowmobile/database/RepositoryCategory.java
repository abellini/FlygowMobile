package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Category;

public class RepositoryCategory extends Repository<Category> {

    private static final String REPOSITORY_CATEGORY = "RepositoryCategory";

    public RepositoryCategory(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Category category) {

        long id = category.getCategoryId();

        Category cat = findById(id);
        if (cat != null) {
            if (cat.getCategoryId() != 0) {
                update(category);
            }
        } else {
            id = insert(category);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Category category) {

        ContentValues values = new ContentValues();
        values.put(Categories.COLUMN_NAME_CATEGORY_ID, category.getCategoryId());
        values.put(Categories.COLUMN_NAME_NAME, category.getName());
        values.put(Categories.COLUMN_NAME_DESCRIPTION, category.getDescription());

        return values;
    }

    @Override
    protected long insert(Category category) {

        ContentValues values = populateContentValues(category);
        long id = db.insert(Categories.TABLE_NAME, "", values);
        Log.i(REPOSITORY_CATEGORY, "Insert [" + id + "] Category record");
        return id;
    }

    @Override
    protected int update(Category category) {

        ContentValues values = populateContentValues(category);
        String _id = String.valueOf(category.getCategoryId());
        String where = Categories.COLUMN_NAME_CATEGORY_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Categories.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_CATEGORY, "Update [" + count + "] Category record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = Categories.COLUMN_NAME_CATEGORY_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Categories.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_CATEGORY, "Delete [" + count + "] Category record(s)");
        return count;
    }

    @Override
    public Category findById(long id) {

        Cursor c = db.query(true, Categories.TABLE_NAME, Category.columns, Categories.COLUMN_NAME_CATEGORY_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            Category category = new Category();
            category.setCategoryId(c.getLong(0));
            category.setName(c.getString(1));
            category.setDescription(c.getString(2));
            return category;
        }
        return null;
    }

    @Override
    public List<Category> listAll() {
        Cursor c = getCursor();
        List<Category> categories = new ArrayList<Category>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Categories.COLUMN_NAME_CATEGORY_ID);
            int idxName = c.getColumnIndex(Categories.COLUMN_NAME_NAME);
            int idxDescription =  c.getColumnIndex(Categories.COLUMN_NAME_DESCRIPTION);

            do {
                Category category = new Category();
                categories.add(category);
                // recupera os atributos de coin
                category.setCategoryId(c.getLong(idxId));
                category.setName(c.getString(idxName));
                category.setDescription(c.getString(idxDescription));
            } while (c.moveToNext());
        }
        return categories;
    }

    @Override
    public Cursor getCursor() {

        return db.query(Categories.TABLE_NAME, Category.columns, null, null, null, null, null, null);
    }

    public static abstract class Categories implements BaseColumns {

        public static final String TABLE_NAME = "Category";

        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO = "photo";
    }


}
