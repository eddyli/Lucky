package com.randalltower605.lucky.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by eli on 4/18/14.
 */
public class Trip implements Parcelable{
  private String mId;
  private Calendar mDeparture;
  private Calendar mArrival;
  private String mFromStationId;
  private String mToStationId;
  private Location mFromLocation;
  private Location mToLocation;
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

  public void setFromStationId(String fromStationId) {
    mFromStationId = fromStationId;
  }

  public void setToStationId(String toStationId) {
    mToStationId = toStationId;
  }

  public Location getFromLocation() {
    return mFromLocation;
  }

  public void setFromLocation(Location fromLocation) {
    mFromLocation = fromLocation;
  }

  public Location getToLocation() {
    return mToLocation;
  }

  public void setToLocation(Location toLocation) {
    mToLocation = toLocation;
  }

  public Trip() {

  }

  public Trip(Parcel in){
    setId(in.readString());
    setFromStationId(in.readString());
    setToStationId(in.readString());

    setDeparture((Calendar)in.readSerializable());
    setArrival((Calendar)in.readSerializable());

    Location from = new Location(Station.LOCATION_PROVIDER);
    from.setLatitude(in.readDouble());
    from.setLongitude(in.readDouble());
    Location to = new Location(Station.LOCATION_PROVIDER);
    to.setLatitude(in.readDouble());
    to.setLongitude(in.readDouble());
    setFromLocation(from);
    setToLocation(to);
  }
  public void writeToParcel(Parcel p, int i) {
    p.writeString(mId);
    p.writeString(mFromStationId);
    p.writeString(mToStationId);
    p.writeSerializable(mDeparture);
    p.writeSerializable(mArrival);
    p.writeDouble(mFromLocation.getLatitude());
    p.writeDouble(mFromLocation.getLongitude());
    p.writeDouble(mToLocation.getLatitude());
    p.writeDouble(mToLocation.getLongitude());
  }

  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Trip> CREATOR
    = new Parcelable.Creator<Trip>() {
    public Trip createFromParcel(Parcel in) {
      return new Trip(in);
    }

    public Trip[] newArray(int size) {
      return new Trip[size];
    }
  };
}
