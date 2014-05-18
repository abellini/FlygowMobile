package br.com.flygowmobile.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.flygowmobile.entity.PaymentForm;

public class RepositoryPaymentForm extends Repository<PaymentForm> {

    private static final String REPOSITORY_PAYMENT = "RepositoryPaymentForm";

    public static abstract class PaymentForms implements BaseColumns {

        private PaymentForms() {}

        public static final String TABLE_NAME = "PaymentForm";

        public static final String DEFAULT_SORT_ORDER = "paymentId ASC";

        public static final String COLUMN_NAME_PAYMENT_ID = "paymentId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

    }

    public RepositoryPaymentForm(Context ctx) {
        db = ctx.openOrCreateDatabase(RepositoryScript.DATABASE_NAME, Context.MODE_PRIVATE, null);
    }


    @Override
    public long save(PaymentForm paymentForm) {
        long id = paymentForm.getPaymentFormId();

        PaymentForm p = findById(id);
        if (p != null) {
            if (p.getPaymentFormId() != 0) {
                this.update(paymentForm);
            }
        } else {
            id = this.insert(paymentForm);
        }
        return id;
    }

    @Override
    protected ContentValues populateContentValues(PaymentForm paymentForm) {

        ContentValues values = new ContentValues();
        values.put(PaymentForms.COLUMN_NAME_PAYMENT_ID, paymentForm.getPaymentFormId());
        values.put(PaymentForms.COLUMN_NAME_NAME, paymentForm.getName());
        values.put(PaymentForms.COLUMN_NAME_DESCRIPTION, paymentForm.getDescription());

        return values;
    }

    @Override
    protected long insert(PaymentForm paymentForm) {
        ContentValues values = populateContentValues(paymentForm);
        long id = db.insert(PaymentForms.TABLE_NAME, "", values);
        Log.i(REPOSITORY_PAYMENT, "Insert [" + id + "] PaymentForm record");
        return id;
    }

    @Override
    protected int update(PaymentForm paymentForm) {

        ContentValues values = populateContentValues(paymentForm);
        String _id = String.valueOf(paymentForm.getPaymentFormId());
        String where = PaymentForms.COLUMN_NAME_PAYMENT_ID + "=?";
        String[] whereArgs = new String[] { _id };
        int count = db.update(PaymentForms.TABLE_NAME, values, where, whereArgs);
        Log.i(REPOSITORY_PAYMENT, "Update [" + count + "] PaymentForm record(s)");
        return count;
    }

    @Override
    public int delete(long id) {

        String where = PaymentForms.COLUMN_NAME_PAYMENT_ID + "=?";
        String _id = String.valueOf(id);
        String[] whereArgs = new String[] { _id };
        int count = db.delete(PaymentForms.TABLE_NAME, where, whereArgs);
        Log.i(REPOSITORY_PAYMENT, "Delete [" + count + "] PaymentForm record(s)");
        return count;
    }

    @Override
    public PaymentForm findById(long id) {
        Cursor c = db.query(true, PaymentForms.TABLE_NAME, PaymentForm.columns, PaymentForms.COLUMN_NAME_PAYMENT_ID + "=" + id, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            PaymentForm paymentForm = new PaymentForm(c.getInt(0), c.getString(1), c.getString(2));
            return paymentForm;
        }
        return null;
    }

    @Override
    public List<PaymentForm> listAll() {
        Cursor c = getCursor();
        List<PaymentForm> paymentForms = new ArrayList<PaymentForm>();
        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(PaymentForms.COLUMN_NAME_PAYMENT_ID);
            int idxName = c.getColumnIndex(PaymentForms.COLUMN_NAME_NAME);
            int idxDescription =  c.getColumnIndex(PaymentForms.COLUMN_NAME_DESCRIPTION);

            do {
                PaymentForm coin = new PaymentForm();
                paymentForms.add(coin);
                // recupera os atributos de Payment Form
                coin.setPaymentFormId(c.getInt(idxId));
                coin.setName(c.getString(idxName));
                coin.setDescription(c.getString(idxDescription));
            } while (c.moveToNext());
        }
        return paymentForms;
    }

    @Override
    public Cursor getCursor() {

        return db.query(PaymentForms.TABLE_NAME, PaymentForm.columns, null, null, null, null, null, null);
    }
}
