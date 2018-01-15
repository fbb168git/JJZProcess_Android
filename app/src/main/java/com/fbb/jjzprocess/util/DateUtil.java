package com.fbb.jjzprocess.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fengbb on 2018/1/2.
 */

public class DateUtil {


    public static String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(new Date());
    }

}
