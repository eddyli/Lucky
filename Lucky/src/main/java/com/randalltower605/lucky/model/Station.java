package com.randalltower605.lucky.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.Geofence;

/**
 * Created by eli on 3/16/14.
 */
public class Station implements Parcelable {
  public static final String LOCATION_PROVIDER = "self";
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

  public double getLatitude() {
    return getLocation().getLatitude();
  }
  public double getLongitude() {
    return getLocation().getLongitude();
  }

  public Geofence toGeofence(int transitionType, int radius, long durationMillis) {
    return new Geofence.Builder()
      .setRequestId(getId())
      .setTransitionTypes(transitionType)
      .setCircularRegion(
        getLatitude(), getLongitude(), radius)
      .setExpirationDuration(durationMillis)
      .build();
  }

  public Station() {
  }

  public Station(Parcel in){
    setId(in.readString());
    setName(in.readString());
    Location l = new Location(LOCATION_PROVIDER);
    l.setLatitude(in.readDouble());
    l.setLongitude(in.readDouble());
    setLocation(l);
    setZone(in.readInt());
  }
  public void writeToParcel(Parcel p, int i) {
    p.writeString(mId);
    p.writeString(mName);
    p.writeDouble(mLocation.getLatitude());
    p.writeDouble(mLocation.getLongitude());
    p.writeInt(mZone);
  }

  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Station> CREATOR
    = new Parcelable.Creator<Station>() {
    public Station createFromParcel(Parcel in) {
      return new Station(in);
    }

    public Station[] newArray(int size) {
      return new Station[size];
    }
  };
}
