package com.matoski.glacier.util;

import com.fasterxml.jackson.databind.util.ISO8601Utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Created by ilijamt on 10/18/15.
 */
public class Parse {

    public static Date ISO8601StringDateParse(final String date) {
        return ISO8601StringDateParse(date, 0);
    }

    public static Date ISO8601StringDateParse(final String date, final int position) {
        Date ret = null;
        try {
            ret = ISO8601Utils.parse(date, new ParsePosition(position));
        } catch (ParseException e) {
            ret = null;
        }
        return ret;
    }
}
