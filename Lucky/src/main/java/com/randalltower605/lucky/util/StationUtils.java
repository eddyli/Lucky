package com.randalltower605.lucky.util;

import android.location.Location;

import com.randalltower605.lucky.model.Station;

/**
 * Created by cheukmanli on 6/26/14.
 */
public class StationUtils {
  /*
   * is station1 south of station2?
   */
  public static boolean isSouthOf(Station s1, Station s2) {
    return s1.getLatitude() < s2.getLatitude();
  }

}
