package com.randalltower605.lucky.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by eli on 3/11/14.
 */
public class FontUtil {
  public static final String GOTHAM_NARROW_BOLD = "Gotham-MSN-NarrowBold.ttf";
  public static final String GOTHAM_NARROW_BOOK = "Gotham-MSN-NarrowBook.ttf";
  public static final String GOTHAM_NARROW_MEDIUM = "GothamNarrow-Medium.otf";
  public static final String GOTHAM_NARROW_LIGHT = "GothamNarrow-Light.otf";

  public static Typeface getTypeFace(AssetManager assetManager, String typeFaceName) {
    return Typeface.createFromAsset(assetManager, typeFaceName);
  }
  public static void func1() {

  }
}
