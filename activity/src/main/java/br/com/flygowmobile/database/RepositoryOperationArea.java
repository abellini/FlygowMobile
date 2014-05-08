package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

import br.com.flygowmobile.entity.OperationArea;

public class RepositoryOperationArea extends Repository<OperationArea> {

    public static abstract class OperationAreas implements BaseColumns {

        public static final String TABLE_NAME = "OperationArea";

        public static final String COLUMN_NAME_OPERATION_AREA_ID = "operationAreaId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }


    @Override
    public long save(OperationArea obj) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(OperationArea obj) {
        return null;
    }

    @Override
    protected long insert(OperationArea obj) {
        return 0;
    }

    @Override
    protected int update(OperationArea obj) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public OperationArea findById(long id) {
        return null;
    }

    @Override
    public List<OperationArea> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }
}
