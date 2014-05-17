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
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import br.com.flygowmobile.Utils.StringUtils;
import br.com.flygowmobile.entity.Attendant;
import br.com.flygowmobile.entity.Coin;

public class RepositoryAttendant extends Repository<Attendant> {

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

        Attendant attend = findById(id);
        if (attend != null) {
            if (attend.getAttendantId() != 0) {
                this.update(attendant);
            }
        } else {
            id = this.insert(attendant);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(Attendant attendant) {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();
        values.put(Attendants.COLUMN_NAME_ATTENDANT_ID, attendant.getAttendantId());
        values.put(Attendants.COLUMN_NAME_NAME, attendant.getName());
        values.put(Attendants.COLUMN_NAME_LAST_NAME, attendant.getLastName());
        values.put(Attendants.COLUMN_NAME_ADDRESS, attendant.getAddress());
        values.put(Attendants.COLUMN_NAME_BIRTH_DATE, fm.format(attendant.getBirthDate()));
        values.put(Attendants.COLUMN_NAME_PHOTO, attendant.getPhoto());
        values.put(Attendants.COLUMN_NAME_LOGIN, attendant.getLogin());
        values.put(Attendants.COLUMN_NAME_PASSWORD, attendant.getPassword());
        values.put(Attendants.COLUMN_NAME_EMAIL, attendant.getEmail());

        return values;
    }

    @Override
    protected long insert(Attendant attendant) {

        ContentValues values = populateContentValues(attendant);
        long id = db.insert(Attendants.TABLE_NAME, "", values);
        Log.i(REPOSITORY_ATTENDANT, "Insert [" + id + "] record");
        return id;
    }

    @Override
    protected int update(Attendant attendant) {

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
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Cursor c = db.query(true, Attendants.TABLE_NAME, Attendant.columns, Attendants.COLUMN_NAME_ATTENDANT_ID + "=" + id, null, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                int idxId = c.getColumnIndex(Attendants.COLUMN_NAME_ATTENDANT_ID);
                int idxName = c.getColumnIndex(Attendants.COLUMN_NAME_NAME);
                int idxLastName =  c.getColumnIndex(Attendants.COLUMN_NAME_LAST_NAME);
                int idxAddress =  c.getColumnIndex(Attendants.COLUMN_NAME_ADDRESS);
                int idxBirthDate =  c.getColumnIndex(Attendants.COLUMN_NAME_BIRTH_DATE);
                int idxPhotoName =  c.getColumnIndex(Attendants.COLUMN_NAME_PHOTO);
                int idxLogin =  c.getColumnIndex(Attendants.COLUMN_NAME_LOGIN);
                int idxPassword =  c.getColumnIndex(Attendants.COLUMN_NAME_PASSWORD);
                int idxEmail =  c.getColumnIndex(Attendants.COLUMN_NAME_EMAIL);


                Attendant attendant = new Attendant();
                attendant.setAttendantId(c.getInt(idxId));
                attendant.setName(c.getString(idxName));
                attendant.setLastName(c.getString(idxLastName));
                attendant.setAddress(c.getString(idxAddress));

                attendant.setBirthDate(fm.parse(c.getString(idxBirthDate)));

                attendant.setPhoto(c.getString(idxPhotoName));
                attendant.setLogin(c.getString(idxLogin));
                attendant.setPassword(c.getString(idxPassword));
                attendant.setEmail(c.getString(idxEmail));



                return attendant;
            }
        } catch (Exception e) {
            Log.e(REPOSITORY_ATTENDANT, "Error [" + e.getMessage() + "]");
        }
        return null;
    }

    @Override
    public List<Attendant> listAll() {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
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
                    attendant.setAttendantId(c.getInt(idxId));
                    attendant.setName(c.getString(idxName));
                    attendant.setLastName(c.getString(idxLastName));
                    attendant.setAddress(c.getString(idxAddress));

                    attendant.setBirthDate(fm.parse(c.getString(idxBirthDate)));

                    attendant.setPhoto(c.getString(idxPhotoName));
                    attendant.setLogin(c.getString(idxLogin));
                    attendant.setPassword(c.getString(idxPassword));
                    attendant.setEmail(c.getString(idxEmail));
                } while (c.moveToNext());
            }
        return attendants;
        } catch (Exception e) {
            Log.e(REPOSITORY_ATTENDANT, "Error [" + e.getMessage() + "]");
        }
        return null;
    }


    @Override
    public Cursor getCursor() {

        return db.query(Attendants.TABLE_NAME, Attendant.columns, null, null, null, null, null, null);
    }

    public Attendant findLast() {
        DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        Cursor c = getCursor();
        try{
            if (c.getCount() > 0) {
                c.moveToLast();
                int idxId = c.getColumnIndex(Attendants.COLUMN_NAME_ATTENDANT_ID);
                int idxName = c.getColumnIndex(Attendants.COLUMN_NAME_NAME);
                int idxLastName =  c.getColumnIndex(Attendants.COLUMN_NAME_LAST_NAME);
                int idxAddress =  c.getColumnIndex(Attendants.COLUMN_NAME_ADDRESS);
                int idxBirthDate =  c.getColumnIndex(Attendants.COLUMN_NAME_BIRTH_DATE);
                int idxPhotoName =  c.getColumnIndex(Attendants.COLUMN_NAME_PHOTO);
                int idxLogin =  c.getColumnIndex(Attendants.COLUMN_NAME_LOGIN);
                int idxPassword =  c.getColumnIndex(Attendants.COLUMN_NAME_PASSWORD);
                int idxEmail =  c.getColumnIndex(Attendants.COLUMN_NAME_EMAIL);
                Attendant attendant = new Attendant();
                attendant.setAttendantId(c.getInt(idxId));
                attendant.setName(c.getString(idxName));
                attendant.setLastName(c.getString(idxLastName));
                attendant.setAddress(c.getString(idxAddress));

                attendant.setBirthDate(fm.parse(c.getString(idxBirthDate)));

                attendant.setPhoto(c.getString(idxPhotoName));
                attendant.setLogin(c.getString(idxLogin));
                attendant.setPassword(c.getString(idxPassword));
                attendant.setEmail(c.getString(idxEmail));
            }
        } catch(Exception e) {
            Log.e(REPOSITORY_ATTENDANT, "Error [" + e.getMessage() + "]");
        }
        return null;
    }
}
