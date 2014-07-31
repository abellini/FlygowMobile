package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Food;
import br.com.flygowmobile.entity.Promotion;

public class RepositoryPromotion extends Repository<Promotion> {

    private static final String REPOSITORY_PROMOTION = "RepositoryPromotion";

    public RepositoryPromotion(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Promotion promotion) {

        long id = promotion.getPromotionId();

        Promotion p = findById(id);
        if (p != null) {
            if (p.getPromotionId() != 0) {
                this.update(promotion);
            }
        } else {
            id = this.insert(promotion);
        }
        return id;
    }

    public int removeAll() {
        int count = db.delete(Promotions.TABLE_NAME, null, null);
        Log.i(REPOSITORY_PROMOTION, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    protected ContentValues populateContentValues(Promotion promotion) {

        ContentValues values = new ContentValues();
        values.put(Promotions.COLUMN_NAME_PROMOTION_ID, promotion.getPromotionId());
        values.put(Promotions.COLUMN_NAME_NAME, promotion.getName());
        values.put(Promotions.COLUMN_NAME_VALUE, promotion.getValue());
        values.put(Promotions.COLUMN_NAME_DESCRIPTION, promotion.getDescription());
        values.put(Promotions.COLUMN_NAME_PHOTO, promotion.getPhoto());
        values.put(Promotions.COLUMN_NAME_PHOTO_NAME, promotion.getPhotoName());
        values.put(Promotions.COLUMN_NAME_VIDEO_NAME, promotion.getVideoName());

        return values;
    }

    @Override
    protected long insert(Promotion promotion) {

        ContentValues values = populateContentValues(promotion);
        long id = db.insert(Promotions.TABLE_NAME, "", values);
        Log.i(REPOSITORY_PROMOTION, "Insert [" + id + "] Promotion records");
        return id;
    }

    @Override
    protected int update(Promotion promotion) {

        ContentValues values = populateContentValues(promotion);
        String _id = String.valueOf(promotion.getPromotionId());
        String where = Promotions.COLUMN_NAME_PROMOTION_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Promotions.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_PROMOTION, "Update [" + count + "] Food record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = Promotions.COLUMN_NAME_PROMOTION_ID  + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Promotions.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_PROMOTION, "Delete [" + count + "] Food record(s)");
        return count;
    }

    @Override
    public Promotion findById(long id) {
        Cursor c = db.query(true, Promotions.TABLE_NAME, Promotion.columns, Promotions.COLUMN_NAME_PROMOTION_ID+ "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            int idxId = c.getColumnIndex(Promotions.COLUMN_NAME_PROMOTION_ID);
            int idxName = c.getColumnIndex(Promotions.COLUMN_NAME_NAME);
            int idxValue =  c.getColumnIndex(Promotions.COLUMN_NAME_VALUE);
            int idxDescription =  c.getColumnIndex(Promotions.COLUMN_NAME_DESCRIPTION);
            int idxPhoto =  c.getColumnIndex(Promotions.COLUMN_NAME_PHOTO);
            int idxPhotoName =  c.getColumnIndex(Promotions.COLUMN_NAME_PHOTO_NAME);
            int idxVideoName =  c.getColumnIndex(Promotions.COLUMN_NAME_VIDEO_NAME);

            Promotion promotion = new Promotion();

            promotion.setPromotionId(c.getInt(idxId));
            promotion.setName(c.getString(idxName));
            promotion.setValue(c.getDouble(idxValue));
            promotion.setDescription(c.getString(idxDescription));
            promotion.setPhoto(c.getBlob(idxPhoto));
            promotion.setPhotoName(c.getString(idxPhotoName));
            promotion.setVideoName(c.getString(idxVideoName));
            return promotion;
        }
        return null;
    }


    @Override
    public List<Promotion> listAll() {
        Cursor c = getCursor();
        List<Promotion> promotions = new ArrayList<Promotion>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            int idxId = c.getColumnIndex(Promotions.COLUMN_NAME_PROMOTION_ID);
            int idxName = c.getColumnIndex(Promotions.COLUMN_NAME_NAME);
            int idxValue =  c.getColumnIndex(Promotions.COLUMN_NAME_VALUE);
            int idxDescription =  c.getColumnIndex(Promotions.COLUMN_NAME_DESCRIPTION);
            int idxPhoto =  c.getColumnIndex(Promotions.COLUMN_NAME_PHOTO);
            int idxPhotoName =  c.getColumnIndex(Promotions.COLUMN_NAME_PHOTO_NAME);
            int idxVideoName =  c.getColumnIndex(Promotions.COLUMN_NAME_VIDEO_NAME);

            do {
                Promotion promotion = new Promotion();

                promotion.setPromotionId(c.getInt(idxId));
                promotion.setName(c.getString(idxName));
                promotion.setValue(c.getDouble(idxValue));
                promotion.setDescription(c.getString(idxDescription));
                promotion.setPhoto(c.getBlob(idxPhoto));
                promotion.setPhotoName(c.getString(idxPhotoName));
                promotion.setVideoName(c.getString(idxVideoName));

                promotions.add(promotion);
            } while (c.moveToNext());
        }
        return promotions;
    }

    @Override
    public Cursor getCursor() {
        return db.query(Promotions.TABLE_NAME, Promotion.columns, null, null, null, null, null, null);
    }

    public static abstract class Promotions implements BaseColumns {

        public static final String TABLE_NAME = "Promotion";

        public static final String COLUMN_NAME_PROMOTION_ID = "promotionId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_PHOTO_NAME = "photoName";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_VIDEO_NAME = "videoName";
    }
}
