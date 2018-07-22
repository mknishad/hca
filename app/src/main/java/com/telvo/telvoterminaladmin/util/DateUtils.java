package com.telvo.telvoterminaladmin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by invar on 07-Nov-17.
 */

public class DateUtils {
    public static String getFormattedDate(String inputDate) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm aa");
        try {
            return output.format(input.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }
    }
}
