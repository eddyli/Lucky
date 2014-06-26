package com.randalltower605.lucky.model;

import java.util.Calendar;

/**
 * Created by cheukmanli on 6/26/14.
 */
public class Stop extends Station {
  private Calendar mDeparture;
  private Calendar mArrival;

  public Calendar getDeparture() {
    return mDeparture;
  }

  public void setDeparture(Calendar departure) {
    mDeparture = departure;
  }

  public Calendar getArrival() {
    return mArrival;
  }

  public void setArrival(Calendar arrival) {
    mArrival = arrival;
  }
}
