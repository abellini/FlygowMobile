package br.com.flygowmobile.Utils;

/**
 * Created by Tiago Rocha Gomes on 01/06/14.
 */
public class ConversorUtils {

    //Moeda base é Real R$
    public static Double convertFromBaseCoin(Double value, Double factor){
        return value / factor;
    }
}
