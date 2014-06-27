package com.randalltower605.lucky.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheukmanli on 6/26/14.
 */
public class DalUtils {

  private static final SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private static final int dayEndsHour = 3;

  private static final Map<Integer, String> dbDayString = new HashMap<Integer, String>();
  static {
    dbDayString.put(1, "sunday");
    dbDayString.put(2, "monday");
    dbDayString.put(3, "tuesday");
    dbDayString.put(4, "wednesday");
    dbDayString.put(5, "thursday");
    dbDayString.put(6, "friday");
    dbDayString.put(7, "saturday");
  }

  public static String getDBTimeString(Calendar calendar) {
    String timeStr = dbTimeFormat.format(calendar.getTime());
    int hour = calendar.get(Calendar.HOUR);
    if(hour >= 0 && hour <= dayEndsHour) {
      timeStr = Integer.toString(24 + hour) + timeStr.substring(2, timeStr.length());
    }
    return timeStr;
  }

  public static String getDBDateString(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR);
    if(hour >= 0 && hour <= dayEndsHour) {
      calendar.add(Calendar.DATE, -1);
    }
    return (dbDateFormat).format(calendar.getTime());
  }

  public static String getDBWeekDayString(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR);
    if(hour >= 0 && hour <= dayEndsHour) {
      calendar.add(Calendar.DATE, -1);
    }
    return dbDayString.get(calendar.get(Calendar.DAY_OF_WEEK));
  }
}
