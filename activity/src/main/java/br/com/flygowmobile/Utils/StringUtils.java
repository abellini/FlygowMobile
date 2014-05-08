package br.com.flygowmobile.Utils;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtils {

    public static final String parseDate(Date date) {

        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        return df.format(date);
    }

}
