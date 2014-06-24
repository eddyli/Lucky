package com.randalltower605.lucky.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.randalltower605.lucky.model.Trip;
import com.randalltower605.lucky.model.Station;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by eli on 4/16/14.
 */
public class StationDal  extends SQLiteAssetHelper {
  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "caltrain.sqlite";

  private Context myContext;

  public StationDal(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    myContext = context;
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    super.onUpgrade(db, oldVersion, newVersion);
  }

  public List<Station> getAllParentStations() {
    List<Station> stations = new ArrayList<Station>();
    // Select All Query
    String selectQuery = "SELECT  * FROM stops WHERE parent_station is null ORDER BY stop_lat desc";

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Station station = new Station();
        station.setId(cursor.getString(0));
        station.setName(cursor.getString(2));
        Location l = new Location("test");
        l.setLatitude(Double.parseDouble(cursor.getString(4)));
        l.setLongitude(Double.parseDouble(cursor.getString(5)));
        station.setLocation(l);
        if(cursor.getString(6) != null) {
            station.setZone(Integer.parseInt(cursor.getString(6)));
        }
        stations.add(station);
      } while (cursor.moveToNext());
    }

    // return contact list
    return stations;
  }

  public List<Trip> getTrips(String fromId, String toId, Calendar today) {
    return getTrips(fromId, toId, today, false);
  }

  public Trip getTrip(String fromId, String toId, Calendar today) {
    List<Trip> trips = getTrips(fromId, toId, today, true);
    return trips != null ? trips.get(0) : null;
  }

  private List<Trip> getTrips(String fromId, String toId, Calendar today, boolean next) {

    //Calendar c = Calendar. today;
    List<Trip> trips = new ArrayList<Trip>();
    String dayFlag = "";
    switch(today.get(Calendar.DAY_OF_WEEK)) {
      case 2:
        dayFlag = "monday";
        break;
      case 3:
        dayFlag = "tuesday";
        break;
      case 4:
        dayFlag = "wednesday";
        break;
      case 5:
        dayFlag = "thursday";
        break;
      case 6:
        dayFlag = "friday";
        break;
      case 7:
        dayFlag = "saturday";
        break;
      case 1:
        dayFlag = "sunday";
        break;
    }
    SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy-MM-dd");
    String strdate = simpleDF.format(today.getTime());

    simpleDF = new SimpleDateFormat("HH:mm:ss");
    String strtime = simpleDF.format(today.getTime());

/*
    String selectQuery = " SELECT c.trip_id, a.stop_name, a1.stop_name, b.departure_time, b1.arrival_time " +
            "FROM stops a, stop_times b, stops a1, stop_times b1, trips c, calendar d " +
            "where a.parent_station='" + fromId + "' and a.stop_id = b.stop_id " +
            "and b.trip_id = b1.trip_id and a1.parent_station='" + toId + "' and a1.stop_id = b1.stop_id " +
            "and b.stop_sequence < b1.stop_sequence " +
            "and b.trip_id = c.trip_id and c.service_id = d.service_id and d." + dayFlag + " = 1 " +
            "and d.start_date <= '" + strdate + "' and d.end_date >= '" + strdate + "' " +
            "order by b.arrival_time";*/

    String selectQuery = " SELECT c.trip_id, a.stop_name, a1.stop_name, b.departure_time, b1.arrival_time " +
      "FROM stops a, stop_times b, stops a1, stop_times b1, trips c, calendar d " +
      "where a.parent_station='%s' and a.stop_id = b.stop_id " +
      "and b.trip_id = b1.trip_id and a1.parent_station='%s' and a1.stop_id = b1.stop_id " +
      "and b.stop_sequence < b1.stop_sequence " +
      "and b.trip_id = c.trip_id and c.service_id = d.service_id and d.%s = 1 " +
      "and d.start_date <= '%s' and d.end_date >= '%s' %s" +
      "order by b.arrival_time";

    String timePart = "and b.departure_time > '" + strtime + "' ";
    String fulFilledSelectQuery = String.format(selectQuery, fromId, toId, dayFlag, strdate, strdate, next ? timePart : "");

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulFilledSelectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Trip trip = new Trip();
        trip.setId(cursor.getString(0));
        trip.setFromStation(cursor.getString(1));
        trip.setToStation(cursor.getString(2));

        try {
          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
          Calendar arrival = Calendar.getInstance();
          arrival.setTime(sdf.parse(cursor.getString(4)));// all done
          trip.setArrival(arrival);

          Calendar departure = Calendar.getInstance();
          departure.setTime(sdf.parse(cursor.getString(3)));// all done
          trip.setDeparture(departure);
        } catch (ParseException e) {

        }

        trips.add(trip);
      } while (cursor.moveToNext());
    }
    /*
     SELECT a.stop_name, a1.stop_name, b.departure_time, b1.arrival_time, c.trip_id
FROM stops a, stop_times b, stops a1, stop_times b1, trips c, calendar d
    where a.parent_station='ctbu' and a.stop_id = b.stop_id and
    b.trip_id = b1.trip_id and a1.parent_station='ctmv' and a1.stop_id = b1.stop_id
   and b.stop_sequence < b1.stop_sequence
   and b.trip_id = c.trip_id and c.service_id = d.service_id and d.monday = 1
  and d.start_date <= '20140415' and d.end_date >= '20140415'
order by b.arrival_time
    * */
    return trips;
  }

  public List<Station> getTripStops(String tripId, Calendar departureTime, Calendar arrivalTime) {
    List<Station> stations = new ArrayList<Station>();
    String selectQuery = "select b.stop_id, b.stop_name, b.stop_lat, b.stop_lon, b.zone_id " +
      "from stop_times a, stops b where a.stop_id = b.stop_id and a.trip_id='%s' and a.departure_time >= '%s' and a.arrival_time <= '%s'";


    SimpleDateFormat simpleDF = new SimpleDateFormat("HH:mm:ss");
    String arrivalTimeStr = simpleDF.format(arrivalTime.getTime());
    String departureTimeStr = simpleDF.format(departureTime.getTime());
    String fulfilledSelectQuery = String.format(selectQuery, tripId, departureTimeStr, arrivalTimeStr);

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulfilledSelectQuery, null);

    // looping through all rows and adding to list
    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Station station = new Station();
        station.setId(cursor.getString(0));
        station.setName(cursor.getString(1));
        Location l = new Location("test");
        l.setLatitude(Double.parseDouble(cursor.getString(2)));
        l.setLongitude(Double.parseDouble(cursor.getString(3)));
        station.setLocation(l);
        if(cursor.getString(4) != null) {
          station.setZone(Integer.parseInt(cursor.getString(4)));
        }
        stations.add(station);
      } while (cursor.moveToNext());
    }

    /*
    select * from stop_times where trip_id='6506115-CT-12OCT-Caltrain-Sunday-02' and stop_sequence < (select stop_sequence from stop_times where trip_id='6506115-CT-12OCT-Caltrain-Sunday-02' and stop_id=70232) and stop_sequence >= (select stop_sequence from stop_times where trip_id='6506115-CT-12OCT-Caltrain-Sunday-02' and stop_id=70142)
     */
    return stations;
  }
}
