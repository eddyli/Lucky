package com.randalltower605.lucky.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by cheukmanli on 5/14/14.
 */
public class DebugUtil {

  private static boolean DEBUG = true;

  public static void showToast(Context context, String text, boolean lengthLong) {
    if(DEBUG && context != null) {
      Toast.makeText(context, text, lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
  }

  public static void showToast(Context context, String text) {
    showToast(context, text, false);
  }
}
