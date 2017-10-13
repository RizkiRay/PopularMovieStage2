package com.rizkiraynaldy.popularmoviestage2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ray on 13/10/17.
 */

public class Util {
    public static String dateFormatter(String date){
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String out = "";
        try {
            Date inDate = inputDateFormat.parse(date);
            out = myDateFormat.format(inDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }
}
