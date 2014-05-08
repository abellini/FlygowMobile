package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.Category;

public class RepositoryCategory extends Repository<Category> {

    public static abstract class Categories implements BaseColumns {

        public static final String TABLE_NAME = "Category";

        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO = "photo";
    }

    @Override
    public long save(Category obj) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(Category obj) {
        return null;
    }

    @Override
    protected long insert(Category obj) {
        return 0;
    }

    @Override
    protected int update(Category obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Category findById(long id) {
        return null;
    }

    @Override
    public List<Category> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }
}
