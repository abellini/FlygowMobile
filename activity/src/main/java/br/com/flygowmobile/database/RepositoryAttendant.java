package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.Attendant;

public class RepositoryAttendant extends Repository<Attendant> {

    final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

    private static final String REPOSITORY_ATTENDANT = "RepositoryAttendant";

    public static abstract class Attendants implements BaseColumns {

        private Attendants() {}

        public static final String TABLE_NAME = "Attendant";

        public static final String DEFAULT_SORT_ORDER = "attendantId ASC";

        public static final String COLUMN_NAME_ATTENDANT_ID = "attendantId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_BIRTH_DATE = "birthDate";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_LOGIN = "login";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_EMAIL = "email";

    }

    public RepositoryAttendant(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public long save(Attendant attendant) {

        long id = attendant.getAttendantId();
        if (id != 0) {
            update(attendant);
        } else {
            id = insert(attendant);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Attendant attendant) {

        ContentValues values = new ContentValues();
        values.put(Attendants.COLUMN_NAME_ATTENDANT_ID, attendant.getAttendantId());
        values.put(Attendants.COLUMN_NAME_NAME, attendant.getName());
        values.put(Attendants.COLUMN_NAME_LAST_NAME, attendant.getLastName());
        values.put(Attendants.COLUMN_NAME_ADDRESS, attendant.getAddress());
        values.put(Attendants.COLUMN_NAME_BIRTH_DATE, parser.format(attendant.getBirthDate()));
        values.put(Attendants.COLUMN_NAME_PHOTO, attendant.getPhoto());
        values.put(Attendants.COLUMN_NAME_LOGIN, attendant.getLogin());
        values.put(Attendants.COLUMN_NAME_PASSWORD, attendant.getPassword());
        values.put(Attendants.COLUMN_NAME_EMAIL, attendant.getEmail());

        return values;
    }

    @Override
    public long insert(Attendant attendant) {

        ContentValues values = populateContentValues(attendant);
        long id = db.insert(Attendants.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ATTENDANT, "Insert [" + id + "] record");
        return id;
    }

    @Override
    public int update(Attendant attendant) {

        ContentValues values = populateContentValues(attendant);
        String _id = String.valueOf(attendant.getAttendantId());
        String where = Attendants.COLUMN_NAME_ATTENDANT_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(Attendants.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_ATTENDANT, "Update [" + count + "] record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = Attendants.COLUMN_NAME_ATTENDANT_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(Attendants.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_ATTENDANT, "Delete [" + count + "] record(s)");
        return count;
    }

    @Override
    public Attendant findById(long id) {

        try {
            Cursor c = db.query(true, Attendants.TABLE_NAME, Attendant.columns, Attendants.COLUMN_NAME_ATTENDANT_ID + "=" + id, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                Attendant attendant = new Attendant(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), parser.parse(c.getString(4)), c.getString(6), c.getString(7), c.getString(8), c.getString(9));
                return attendant;
            }
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public List<Attendant> listAll() {
        try {
            Cursor c = getCursor();
            List<Attendant> attendants = new ArrayList<Attendant>();
            if (c.moveToFirst()) {
                int idxId = c.getColumnIndex(Attendants.COLUMN_NAME_ATTENDANT_ID);
                int idxName = c.getColumnIndex(Attendants.COLUMN_NAME_NAME);
                int idxLastName =  c.getColumnIndex(Attendants.COLUMN_NAME_LAST_NAME);
                int idxAddress =  c.getColumnIndex(Attendants.COLUMN_NAME_ADDRESS);
                int idxBirthDate =  c.getColumnIndex(Attendants.COLUMN_NAME_BIRTH_DATE);
                int idxPhotoName =  c.getColumnIndex(Attendants.COLUMN_NAME_PHOTO);
                int idxLogin =  c.getColumnIndex(Attendants.COLUMN_NAME_LOGIN);
                int idxPassword =  c.getColumnIndex(Attendants.COLUMN_NAME_PASSWORD);
                int idxEmail =  c.getColumnIndex(Attendants.COLUMN_NAME_EMAIL);

                do {
                    Attendant attendant = new Attendant();
                    attendants.add(attendant);
                    // recupera os atributos de coin
                    attendant.setAttendantId(c.getLong(idxId));
                    attendant.setName(c.getString(idxName));
                    attendant.setLastName(c.getString(idxLastName));
                    attendant.setAddress(c.getString(idxAddress));

                    attendant.setBirthDate(parser.parse(c.getString(idxBirthDate)));

                    attendant.setPhoto(c.getString(idxPhotoName));
                    attendant.setLogin(c.getString(idxLogin));
                    attendant.setPassword(c.getString(idxPassword));
                    attendant.setEmail(c.getString(idxEmail));
                } while (c.moveToNext());
            }
        return attendants;
        } catch (Exception e) {

        }
        return null;
    }

//    @Override
//    public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
//        return null;
//    }

    @Override
    public Cursor getCursor() {

        return db.query(Attendants.TABLE_NAME, Attendant.columns, null, null, null, null, null, null);
    }
}
