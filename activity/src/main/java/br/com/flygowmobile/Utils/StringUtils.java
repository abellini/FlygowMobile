package br.com.flygowmobile.Utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.sss");

    public final String parseDate(Date date) {

        return parser.format(date);
    }

}
