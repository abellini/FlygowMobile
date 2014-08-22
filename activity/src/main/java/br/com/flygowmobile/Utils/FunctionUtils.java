package br.com.flygowmobile.Utils;

import java.text.DecimalFormat;

import br.com.flygowmobile.entity.Coin;

/**
 * Created by Tiago Rocha Gomes on 21/08/14.
 */
public class FunctionUtils {

    private final static String BLANK_SPACE = " ";

    public static String addZero(int value){
        if(value < 10){
            return "0" + value;
        }
        return value + "";
    }

    public static String getMonetaryString(Coin coin, Double totalOrderValue) {
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        return coin.getSymbol() + BLANK_SPACE + df.format(totalOrderValue);
    }
}
