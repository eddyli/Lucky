package com.randalltower605.lucky.model;

import android.location.Location;

/**
 * Created by eli on 3/16/14.
 */
public class Station {
  private String mId;
  private String mName;
  private Location mLocation;
  private int mZone;

  public String getId() {
    return mId;
  }

  public void setId(String mId) {
    this.mId = mId;
  }

  public String getName() {
    return mName;
  }

  public void setName(String mName) {
    this.mName = mName;
  }

  public Location getLocation() {
    return mLocation;
  }

  public void setLocation(Location mLocation) {
    this.mLocation = mLocation;
  }

  public int getZone() {
    return mZone;
  }

  public void setZone(int mZone) {
    this.mZone = mZone;
  }

  public String toString() {
    return this.mName;
  }
}
