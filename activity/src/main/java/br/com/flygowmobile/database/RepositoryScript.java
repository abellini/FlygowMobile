package br.com.flygowmobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class RepositoryScript {


    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FlyGow.db";



    public static abstract class Coin implements BaseColumns {

        public static final String TABLE_NAME = "Coin";

        public static final String COLUMN_NAME_COIN_ID = "coinId";
        public static final String COLUMN_NAME_SYMBOL_ID = "symbol";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CONVERSION = "conversion";

    }

    public static abstract class Advertisement implements BaseColumns {

        public static final String TABLE_NAME = "Advertisement";

        public static final String COLUMN_NAME_ADVERTISEMENT_ID = "advertisementId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_INITIAL_DATE = "initialDate";
        public static final String COLUMN_NAME_FINAL_DATE = "finalDate";
        public static final String COLUMN_NAME_IS_ACTIVE = "isActive";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_VIDEO = "video";

    }

    public static abstract class Order implements BaseColumns {

        public static final String TABLE_NAME = "OrderTab";

        public static final String COLUMN_NAME_ORDER_ID = "orderId";
        public static final String COLUMN_NAME_CLIENT_ID = "clientId";
        public static final String COLUMN_NAME_TABLET_ID = "tabletId";
        public static final String COLUMN_NAME_TOTAL_VALUE = "totalValue";
        public static final String COLUMN_NAME_ORDER_HOUR = "orderHour";
        public static final String COLUMN_NAME_ATTENDANT_ID = "attendantId";

    }

    public static abstract class OrderItem implements BaseColumns {

        public static final String TABLE_NAME = "OrderItem";

        public static final String COLUMN_NAME_ORDER_ITEM_ID = "orderItemId";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_OBSERVATIONS = "observations";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_FOOD_ID = "foodId";
        public static final String COLUMN_NAME_ORDER_ID = "orderId";

    }

    public static abstract class Category implements BaseColumns {

        public static final String TABLE_NAME = "Category";

        public static final String COLUMN_NAME_CATEGORY_ID = "categoryId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO = "photo";
    }

    public static abstract class Food implements BaseColumns {

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

    public static abstract class OperationArea implements BaseColumns {

        public static final String TABLE_NAME = "OperationArea";

        public static final String COLUMN_NAME_OPERATION_AREA_ID = "operationAreaId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    private static final String[] SCRIPT_CREATE_TABLES = new String[] {
            "CREATE TABLE " + RepositoryTablet.Tablets.TABLE_NAME + " (" +
                    RepositoryTablet.Tablets.COLUMN_NAME_TABLET_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryTablet.Tablets.COLUMN_NAME_STATUS_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_COIN_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_IP + TEXT_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_PORT + INTEGER_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_SERVER_IP + TEXT_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_SERVER_PORT + INTEGER_TYPE + COMMA_SEP +
                    RepositoryTablet.Tablets.COLUMN_NAME_ATTENDANT_ID + INTEGER_TYPE +
            " )",
            "CREATE TABLE " + Coin.TABLE_NAME + " (" +
                    Coin.COLUMN_NAME_COIN_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    Coin.COLUMN_NAME_SYMBOL_ID + TEXT_TYPE + COMMA_SEP +
                    Coin.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Coin.COLUMN_NAME_CONVERSION + REAL_TYPE +
            " )",
            "CREATE TABLE " + Advertisement.TABLE_NAME + " (" +
                    Advertisement.COLUMN_NAME_ADVERTISEMENT_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    Advertisement.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Advertisement.COLUMN_NAME_INITIAL_DATE + TEXT_TYPE + COMMA_SEP +
                    Advertisement.COLUMN_NAME_FINAL_DATE + TEXT_TYPE + COMMA_SEP +
                    Advertisement.COLUMN_NAME_IS_ACTIVE + INTEGER_TYPE + COMMA_SEP +
                    Advertisement.COLUMN_NAME_PHOTO + BLOB_TYPE + COMMA_SEP +
                    Advertisement.COLUMN_NAME_VIDEO + BLOB_TYPE +
            " )",
            "CREATE TABLE " + Order.TABLE_NAME + " (" +
                    Order.COLUMN_NAME_ORDER_ID + INTEGER_TYPE + " PRIMARY KEY,"+
                    Order.COLUMN_NAME_CLIENT_ID + INTEGER_TYPE + COMMA_SEP +
                    Order.COLUMN_NAME_TABLET_ID + INTEGER_TYPE + COMMA_SEP +
                    Order.COLUMN_NAME_TOTAL_VALUE + REAL_TYPE + COMMA_SEP +
                    Order.COLUMN_NAME_ORDER_HOUR + TEXT_TYPE + COMMA_SEP +
                    Order.COLUMN_NAME_ATTENDANT_ID + INTEGER_TYPE +
            " )",
            "CREATE TABLE " + OrderItem.TABLE_NAME + " (" +
                    OrderItem.COLUMN_NAME_ORDER_ITEM_ID + INTEGER_TYPE + " PRIMARY KEY,"+
                    OrderItem.COLUMN_NAME_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                    OrderItem.COLUMN_NAME_OBSERVATIONS + TEXT_TYPE + COMMA_SEP +
                    OrderItem.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    OrderItem.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + COMMA_SEP +
                    OrderItem.COLUMN_NAME_ORDER_ID + INTEGER_TYPE +
            " )",
            "CREATE TABLE " + Category.TABLE_NAME + " (" +
                    Category.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    Category.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Category.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Category.COLUMN_NAME_PHOTO + BLOB_TYPE +
            " )",
            "CREATE TABLE " + Food.TABLE_NAME + " (" +
                    Food.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    Food.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_PHOTO + BLOB_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_VIDEO + BLOB_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_OPERATION_AREA_ID + INTEGER_TYPE + COMMA_SEP +
                    Food.COLUMN_NAME_NUTRITIONAL_INFO + TEXT_TYPE + COMMA_SEP+
                    Food.COLUMN_NAME_IS_ACTIVE + INTEGER_TYPE +
            " )",
            "CREATE TABLE " + OperationArea.TABLE_NAME + " (" +
                    OperationArea.COLUMN_NAME_OPERATION_AREA_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    OperationArea.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    OperationArea.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
            " )"
    };

    private static final String[] SCRIPT_DELETE_TABLES = new String[] {
            "DROP TABLE IF EXISTS " + Coin.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryTablet.Tablets.TABLE_NAME,
            "DROP TABLE IF EXISTS " + Advertisement.TABLE_NAME,
            "DROP TABLE IF EXISTS " + Order.TABLE_NAME,
            "DROP TABLE IF EXISTS " + OrderItem.TABLE_NAME,
            "DROP TABLE IF EXISTS " + Category.TABLE_NAME,
            "DROP TABLE IF EXISTS " + Food.TABLE_NAME,
            "DROP TABLE IF EXISTS " + OperationArea.TABLE_NAME
    };

    private SQLiteHelper dbHelper;
    protected SQLiteDatabase db;

    public RepositoryScript(Context ctx){
        dbHelper = new SQLiteHelper(ctx, DATABASE_NAME, DATABASE_VERSION, SCRIPT_CREATE_TABLES, SCRIPT_DELETE_TABLES);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }
}
