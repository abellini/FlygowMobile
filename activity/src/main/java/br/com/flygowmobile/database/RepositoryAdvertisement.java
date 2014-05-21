package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Advertisement;

public class RepositoryAdvertisement extends Repository<Advertisement> {

    private static final String REPOSITORY_ADVERTISEMENT = "RepositoryAdvertisement";

    public RepositoryAdvertisement(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Advertisement advertisement) {
        long id = advertisement.getAdvertisementId();

        Advertisement adv = findById(id);
        if (adv != null) {
            if (adv.getAdvertisementId() != 0) {
                this.update(advertisement);
            }
        } else {
            id = this.insert(advertisement);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Advertisement advertisement) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put(Advertisements.COLUMN_NAME_ADVERTISEMENT_ID, advertisement.getAdvertisementId());
        values.put(Advertisements.COLUMN_NAME_TABLET_ID, advertisement.getTabletId());
        values.put(Advertisements.COLUMN_NAME_NAME, advertisement.getName());
        values.put(Advertisements.COLUMN_NAME_IS_ACTIVE, advertisement.isActive());
        values.put(Advertisements.COLUMN_NAME_INITIAL_DATE, fm.format(advertisement.getInicialDate()));
        values.put(Advertisements.COLUMN_NAME_FINAL_DATE, fm.format(advertisement.getFinalDate()));
        values.put(Advertisements.COLUMN_NAME_PHOTO, advertisement.getPhotoName());
        values.put(Advertisements.COLUMN_NAME_VIDEO, advertisement.getVideoName());

        return values;
    }

    @Override
    protected long insert(Advertisement advertisement) {

        ContentValues values = populateContentValues(advertisement);
        long id = db.insert(Advertisements.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ADVERTISEMENT, "Insert [" + id + "] Advertisement record");
        return id;
    }

    @Override
    protected int update(Advertisement advertisement) {

        ContentValues values = populateContentValues(advertisement);
        String _id = String.valueOf(advertisement.getAdvertisementId());
        String where = Advertisements.COLUMN_NAME_ADVERTISEMENT_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Advertisements.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_ADVERTISEMENT, "Update [" + count + "] Advertisement record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = Advertisements.COLUMN_NAME_ADVERTISEMENT_ID+ "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Advertisements.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ADVERTISEMENT, "Delete [" + count + "] Advertisement record(s)");
        return count;
    }

    public int removeAll() {
        int count = db.delete(Advertisements.TABLE_NAME, null, null);
        Log.i(REPOSITORY_ADVERTISEMENT, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    public Advertisement findById(long id) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Cursor c = db.query(true, Advertisements.TABLE_NAME, Advertisement.columns, Advertisements.COLUMN_NAME_ADVERTISEMENT_ID + "=" + id, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                int idxId = c.getColumnIndex(Advertisements.COLUMN_NAME_ADVERTISEMENT_ID);
                int idxName = c.getColumnIndex(Advertisements.COLUMN_NAME_NAME);
                int idxTabletId =  c.getColumnIndex(Advertisements.COLUMN_NAME_TABLET_ID);
                int idxIniDate =  c.getColumnIndex(Advertisements.COLUMN_NAME_INITIAL_DATE);
                int idxFinalDate =  c.getColumnIndex(Advertisements.COLUMN_NAME_FINAL_DATE);
                int idxIsActive =  c.getColumnIndex(Advertisements.COLUMN_NAME_IS_ACTIVE);
                int idxPhotoName =  c.getColumnIndex(Advertisements.COLUMN_NAME_PHOTO);
                int idxVideoName =  c.getColumnIndex(Advertisements.COLUMN_NAME_VIDEO);


                Advertisement advertisement = new Advertisement();
                advertisement.setAdvertisementId(c.getInt(idxId));
                advertisement.setTabletId(c.getLong(idxTabletId));
                advertisement.setName(c.getString(idxName));
                advertisement.setActive(Boolean.parseBoolean(c.getString(idxIsActive)));
                advertisement.setInicialDate(fm.parse(c.getString(idxIniDate)));
                advertisement.setFinalDate(fm.parse(c.getString(idxFinalDate)));
                advertisement.setPhotoName(c.getString(idxPhotoName));
                advertisement.setVideoName(c.getString(idxVideoName));

                return advertisement;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ADVERTISEMENT, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    @Override
    public List<Advertisement> listAll() {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        List<Advertisement> advertisements = new ArrayList<Advertisement>();
        try {
            Cursor c = getCursor();
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(Advertisements.COLUMN_NAME_ADVERTISEMENT_ID);
                int idxName = c.getColumnIndex(Advertisements.COLUMN_NAME_NAME);
                int idxTabletId =  c.getColumnIndex(Advertisements.COLUMN_NAME_TABLET_ID);
                int idxIniDate =  c.getColumnIndex(Advertisements.COLUMN_NAME_INITIAL_DATE);
                int idxFinalDate =  c.getColumnIndex(Advertisements.COLUMN_NAME_FINAL_DATE);
                int idxIsActive =  c.getColumnIndex(Advertisements.COLUMN_NAME_IS_ACTIVE);
                int idxPhotoName =  c.getColumnIndex(Advertisements.COLUMN_NAME_PHOTO);
                int idxVideoName =  c.getColumnIndex(Advertisements.COLUMN_NAME_VIDEO);

                do {
                    Advertisement advertisement = new Advertisement();
                    advertisement.setAdvertisementId(c.getInt(idxId));
                    advertisement.setTabletId(c.getLong(idxTabletId));
                    advertisement.setName(c.getString(idxName));
                    advertisement.setActive(Boolean.parseBoolean(c.getString(idxIsActive)));
                    advertisement.setInicialDate(fm.parse(c.getString(idxIniDate)));
                    advertisement.setFinalDate(fm.parse(c.getString(idxFinalDate)));
                    advertisement.setPhotoName(c.getString(idxPhotoName));
                    advertisement.setVideoName(c.getString(idxVideoName));

                    advertisements.add(advertisement);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ADVERTISEMENT, "Error [" + e.getMessage() + "]");
        }
        return advertisements;
    }

    @Override
    public Cursor getCursor() {

        return db.query(Advertisements.TABLE_NAME, Advertisement.columns, null, null, null, null, null, null);
    }

    public static abstract class Advertisements implements BaseColumns {

        public static final String TABLE_NAME = "Advertisement";

        public static final String COLUMN_NAME_ADVERTISEMENT_ID = "advertisementId";
        public static final String COLUMN_NAME_TABLET_ID = "tabletId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_INITIAL_DATE = "initialDate";
        public static final String COLUMN_NAME_FINAL_DATE = "finalDate";
        public static final String COLUMN_NAME_IS_ACTIVE = "isActive";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_VIDEO = "video";

    }


}
