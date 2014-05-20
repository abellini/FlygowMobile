package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryAccompaniment;

public class Accompaniment implements Serializable {

    public static String[] columns = new String[] {
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_ACCOMPANIMENT_ID,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_NAME,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_VALUE,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_DESCRIPTION,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_IS_ACTIVE,
            RepositoryAccompaniment.Accompaniments.COLUMN_NAME_CATEGORY_ID
    };
    private int id;
    private String name;
    private Double value;
    private String description;
    private boolean active;
    private int categoryId;

    public Accompaniment() {}


}
