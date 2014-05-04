package br.com.flygowmobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String SQLLITEHELPER = "SQLiteHelper";
    private String[] scriptSQLCreate;
    private String[] scriptSQLDelete;

    public SQLiteHelper(Context context, String nameDataBase, int versionDataBase, String[] scriptSQLCreate, String[] scriptSQLDelete) {
        super(context, nameDataBase, null, versionDataBase);
        this.scriptSQLCreate = scriptSQLCreate;
        this.scriptSQLDelete = scriptSQLDelete;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(SQLLITEHELPER, "Creating database with SQL");

        int qtdeScriptCreate = scriptSQLCreate.length;
        for (int i=0; i<qtdeScriptCreate; i++) {
            String sql = scriptSQLCreate[i];
            Log.i(SQLLITEHELPER, sql);
            db.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(SQLLITEHELPER, "Updating version " + oldVersion + " to " + newVersion + ". All records are deleted.");

        int qtdeScriptDelete = scriptSQLDelete.length;
        for (int i=0; i<qtdeScriptDelete; i++) {
            String sql = scriptSQLDelete[i];
            Log.i(SQLLITEHELPER, sql);
            db.execSQL(sql);
        }
        onCreate(db);


    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
