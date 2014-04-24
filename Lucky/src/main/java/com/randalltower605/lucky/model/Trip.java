package com.randalltower605.lucky.model;

import java.util.Calendar;

/**
 * Created by eli on 4/18/14.
 */
public class Trip {
  private String mId;
  private Calendar mDeparture;
  private Calendar mArrival;
  private String mFromStationId;
  private String mToStationId;
  private float mFare;

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    this.mId = id;
  }

  public Calendar getDeparture() {
    return mDeparture;
  }

  public void setDeparture(Calendar departure) {
    this.mDeparture = departure;
  }

  public Calendar getArrival() {
    return mArrival;
  }

  public void setArrival(Calendar arrival) {
    this.mArrival = arrival;
  }

  public String getFromStationId() {
    return mFromStationId;
  }

  public void setFromStation(String fromStationId) {
    this.mFromStationId = fromStationId;
  }

  public String getToStationId() {
    return mToStationId;
  }

  public void setToStation(String toStationId) {
    this.mToStationId = toStationId;
  }
}
