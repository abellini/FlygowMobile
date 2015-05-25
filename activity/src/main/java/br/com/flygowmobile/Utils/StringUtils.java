package br.com.flygowmobile.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    public static final Date parseDate(String date) {

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String parseString(Date date) {
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static boolean isNotEmpty(String str){
        if(str == null || str.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
