package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.Advertisement;

public class RepositoryAdvertisement extends Repository<Advertisement> {

    public static abstract class Advertisements implements BaseColumns {

        public static final String TABLE_NAME = "Advertisement";

        public static final String COLUMN_NAME_ADVERTISEMENT_ID = "advertisementId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_INITIAL_DATE = "initialDate";
        public static final String COLUMN_NAME_FINAL_DATE = "finalDate";
        public static final String COLUMN_NAME_IS_ACTIVE = "isActive";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_VIDEO = "video";

    }

    @Override
    public long save(Advertisement obj) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(Advertisement obj) {
        return null;
    }

    @Override
    protected long insert(Advertisement obj) {
        return 0;
    }

    @Override
    protected int update(Advertisement obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Advertisement findById(long id) {
        return null;
    }

    @Override
    public List<Advertisement> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }
}
