package com.telvo.telvoterminaladmin.util;

import java.text.DecimalFormat;

/**
 * Created by invar on 16-Nov-17.
 */

public class NumberFormatUtils {

    public static String getFormattedDouble(Double amount) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(amount);
    }
}
