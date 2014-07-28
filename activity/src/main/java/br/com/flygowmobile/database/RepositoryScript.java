package br.com.flygowmobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class RepositoryScript {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FlyGow.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String BLOB_TYPE = " BLOB";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";

    private static final String[] SCRIPT_CREATE_TABLES = new String[]{
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
            "CREATE TABLE " + RepositoryCoin.Coins.TABLE_NAME + " (" +
                    RepositoryCoin.Coins.COLUMN_NAME_COIN_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryCoin.Coins.COLUMN_NAME_SYMBOL + TEXT_TYPE + COMMA_SEP +
                    RepositoryCoin.Coins.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryCoin.Coins.COLUMN_NAME_CONVERSION + REAL_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryAdvertisement.Advertisements.TABLE_NAME + " (" +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_ADVERTISEMENT_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_TABLET_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_INITIAL_DATE + TEXT_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_FINAL_DATE + TEXT_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_IS_ACTIVE + INTEGER_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_PHOTO_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_PHOTO + BLOB_TYPE + COMMA_SEP +
                    RepositoryAdvertisement.Advertisements.COLUMN_NAME_VIDEO_NAME + TEXT_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryOrder.Orders.TABLE_NAME + " (" +
                    RepositoryOrder.Orders.COLUMN_NAME_ORDER_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryOrder.Orders.COLUMN_NAME_CLIENT_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryOrder.Orders.COLUMN_NAME_TABLET_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryOrder.Orders.COLUMN_NAME_TOTAL_VALUE + REAL_TYPE + COMMA_SEP +
                    RepositoryOrder.Orders.COLUMN_NAME_ORDER_HOUR + TEXT_TYPE + COMMA_SEP +
                    RepositoryOrder.Orders.COLUMN_NAME_ATTENDANT_ID + INTEGER_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryOrderItem.OrderItems.TABLE_NAME + " (" +
                    RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ITEM_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryOrderItem.OrderItems.COLUMN_NAME_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                    RepositoryOrderItem.OrderItems.COLUMN_NAME_OBSERVATIONS + TEXT_TYPE + COMMA_SEP +
                    RepositoryOrderItem.OrderItems.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    RepositoryOrderItem.OrderItems.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryOrderItem.OrderItems.COLUMN_NAME_ORDER_ID + INTEGER_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryCategory.Categories.TABLE_NAME + " (" +
                    RepositoryCategory.Categories.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryCategory.Categories.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryCategory.Categories.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RepositoryCategory.Categories.COLUMN_NAME_PHOTO + BLOB_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryFood.Foods.TABLE_NAME + " (" +
                    RepositoryFood.Foods.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryFood.Foods.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_PHOTO + BLOB_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_VIDEO + BLOB_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_OPERATION_AREA_ID + INTEGER_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_NUTRITIONAL_INFO + TEXT_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_IS_ACTIVE + INTEGER_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_PHOTO_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryFood.Foods.COLUMN_NAME_VIDEO_NAME + TEXT_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryOperationArea.OperationAreas.TABLE_NAME + " (" +
                    RepositoryOperationArea.OperationAreas.COLUMN_NAME_OPERATION_AREA_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryOperationArea.OperationAreas.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryOperationArea.OperationAreas.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryAttendant.Attendants.TABLE_NAME + " (" +
                    RepositoryAttendant.Attendants.COLUMN_NAME_ATTENDANT_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryAttendant.Attendants.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_BIRTH_DATE + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_PHOTO + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_LOGIN + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    RepositoryAttendant.Attendants.COLUMN_NAME_EMAIL + TEXT_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryPaymentForm.PaymentForms.TABLE_NAME + " (" +
                    RepositoryPaymentForm.PaymentForms.COLUMN_NAME_PAYMENT_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryPaymentForm.PaymentForms.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryPaymentForm.PaymentForms.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryAccompaniment.Accompaniments.TABLE_NAME + " (" +
                    RepositoryAccompaniment.Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryAccompaniment.Accompaniments.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryAccompaniment.Accompaniments.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    RepositoryAccompaniment.Accompaniments.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RepositoryAccompaniment.Accompaniments.COLUMN_NAME_IS_ACTIVE + INTEGER_TYPE + COMMA_SEP +
                    RepositoryAccompaniment.Accompaniments.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryFoodAccompaniment.FoodAccompaniments.TABLE_NAME + " (" +
                    RepositoryFoodAccompaniment.FoodAccompaniments.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    RepositoryFoodAccompaniment.FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    " PRIMARY KEY (" + RepositoryFoodAccompaniment.FoodAccompaniments.COLUMN_NAME_FOOD_ID + ", " + RepositoryFoodAccompaniment.FoodAccompaniments.COLUMN_NAME_ACCOMPANIMENT_ID + ")" +
                    " )",
            "CREATE TABLE " + RepositoryPromotion.Promotions.TABLE_NAME + " (" +
                    RepositoryPromotion.Promotions.COLUMN_NAME_PROMOTION_ID + INTEGER_TYPE + " PRIMARY KEY," +
                    RepositoryPromotion.Promotions.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    RepositoryPromotion.Promotions.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    RepositoryPromotion.Promotions.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    RepositoryPromotion.Promotions.COLUMN_NAME_PHOTO + BLOB_TYPE + COMMA_SEP +
                    RepositoryPromotion.Promotions.COLUMN_NAME_VIDEO + BLOB_TYPE +
                    " )",
            "CREATE TABLE " + RepositoryFoodPromotion.FoodPromotions.TABLE_NAME + " (" +
                    RepositoryFoodPromotion.FoodPromotions.COLUMN_NAME_FOOD_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    RepositoryFoodPromotion.FoodPromotions.COLUMN_NAME_PROMOTION_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    " PRIMARY KEY (" + RepositoryFoodPromotion.FoodPromotions.COLUMN_NAME_FOOD_ID + ", " + RepositoryFoodPromotion.FoodPromotions.COLUMN_NAME_PROMOTION_ID + ")" +
                    " )"
    };

    private static final String[] SCRIPT_DELETE_TABLES = new String[]{
            "DROP TABLE IF EXISTS " + RepositoryCoin.Coins.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryTablet.Tablets.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryAttendant.Attendants.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryOperationArea.OperationAreas.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryFood.Foods.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryCategory.Categories.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryAdvertisement.Advertisements.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryOrder.Orders.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryOrderItem.OrderItems.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryPaymentForm.PaymentForms.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryAccompaniment.Accompaniments.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryFoodAccompaniment.FoodAccompaniments.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryPromotion.Promotions.TABLE_NAME,
            "DROP TABLE IF EXISTS " + RepositoryFoodPromotion.FoodPromotions.TABLE_NAME
    };
    protected SQLiteDatabase db;
    private SQLiteHelper dbHelper;

    public RepositoryScript(Context ctx) {
        dbHelper = new SQLiteHelper(ctx, DATABASE_NAME, DATABASE_VERSION, SCRIPT_CREATE_TABLES, SCRIPT_DELETE_TABLES);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}