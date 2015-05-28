package br.com.flygowmobile.entity;

import java.io.Serializable;

import br.com.flygowmobile.database.RepositoryCoin;

public class Coin implements Serializable {

    public static String[] columns = new String[] {
            RepositoryCoin.Coins.COLUMN_NAME_COIN_ID,
            RepositoryCoin.Coins.COLUMN_NAME_NAME,
            RepositoryCoin.Coins.COLUMN_NAME_SYMBOL,
            RepositoryCoin.Coins.COLUMN_NAME_CONVERSION
    };

    private int coinId;
    private String name;
    private String symbol;
    private double conversion;

    public Coin() {}

    public Coin(int coinId, String name, String symbol, double conversion) {
        this.setCoinId(coinId);
        this.setName(name);
        this.setSymbol(symbol);
        this.setConversion(conversion);
    }


    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    public String toJSON() {
        return "{" +
                "\"coinId\": "+ getCoinId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"symbol\": " + getSymbol() + ", " +
                "\"conversion\": " + "\"" + getConversion() +
                "}";
    }
}
