package br.com.flygowmobile.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import br.com.flygowmobile.entity.Client;

/**
 * Created by alexandre on 8/12/14.
 */
public class RepositoryClient extends Repository<Client> {

    @Override
    public long save(Client client) {
        return 0;
    }

    @Override
    protected ContentValues populateContentValues(Client client) {
        return null;
    }

    @Override
    protected long insert(Client client) {
        return 0;
    }

    @Override
    protected int update(Client client) {
        return 0;
    }

    @Override
    public int delete(long id) {
        return 0;
    }

    @Override
    public Client findById(long id) {
        return null;
    }

    @Override
    public List<Client> listAll() {
        return null;
    }

    @Override
    public Cursor getCursor() {
        return null;
    }
}
