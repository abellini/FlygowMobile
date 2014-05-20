package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.Accompaniment;

public class RepositoryAccompaniment extends Repository<Accompaniment> {

    @Override
    public long save(Accompaniment obj) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(Accompaniment obj) {
        return null;
    }

    @Override
    protected long insert(Accompaniment obj) {
        return 0;
    }

    @Override
    protected int update(Accompaniment obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Accompaniment findById(long id) {
        return null;
    }

    @Override
    public List<Accompaniment> listAll() {
        return null;
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
