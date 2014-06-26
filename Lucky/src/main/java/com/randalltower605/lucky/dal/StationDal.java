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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eli on 4/16/14.
 */
public class StationDal  extends SQLiteAssetHelper {
  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "caltrain.sqlite";

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
  //caltrain db day don't end at 00, overnight train hour could exceed 24. for example 1am = 25, etc.
  private static final int dayEndsHour = 3;
  private static final SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm:ss");
  private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public StationDal(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    super.onUpgrade(db, oldVersion, newVersion);
  }

  private String getDBTimeString(Calendar calendar) {
    String timeStr = dbTimeFormat.format(calendar.getTime());
    int hour = calendar.get(Calendar.HOUR);
    if(hour >= 0 && hour <= dayEndsHour) {
        timeStr = Integer.toString(24 + hour) + timeStr.substring(2, timeStr.length());
    }
    return timeStr;
  }

  private String getDBDateString(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR);
    if(hour >= 0 && hour <= dayEndsHour) {
      calendar.add(Calendar.DATE, -1);
    }
    return (dbDateFormat).format(calendar.getTime());
  }

  private String getDBWeekDayString(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR);
    if(hour >= 0 && hour <= dayEndsHour) {
      calendar.add(Calendar.DATE, -1);
    }
    return dbDayString.get(calendar.get(Calendar.DAY_OF_WEEK));
  }

  public List<Station> getAllParentStations() {
    List<Station> stations = new ArrayList<Station>();
    String selectQuery = "SELECT  * FROM stops WHERE parent_station is null ORDER BY stop_lat desc";

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        Station station = new Station();
        station.setId(cursor.getString(0));
        station.setName(cursor.getString(2));
        Location l = new Location(Station.LOCATION_PROVIDER);
        l.setLatitude(Double.parseDouble(cursor.getString(4)));
        l.setLongitude(Double.parseDouble(cursor.getString(5)));
        station.setLocation(l);
        if(cursor.getString(6) != null) {
            station.setZone(Integer.parseInt(cursor.getString(6)));
        }
        stations.add(station);
      } while (cursor.moveToNext());
    }

    db.close();

    // return contact list
    return stations;
  }

  public List<Trip> getTrips(String fromId, String toId, Calendar today) {
    return getTrips(fromId, toId, today, false);
  }

  /*

   */
  private List<Trip> getTrips(String fromId, String toId, Calendar calendar, boolean next) {

    List<Trip> trips = new ArrayList<Trip>();

    String selectQuery = " SELECT c.trip_id, a.stop_name, a1.stop_name, b.departure_time, b1.arrival_time " +
      "FROM stops a, stop_times b, stops a1, stop_times b1, trips c, calendar d " +
      "where a.parent_station='%s' and a.stop_id = b.stop_id " +
      "and b.trip_id = b1.trip_id and a1.parent_station='%s' and a1.stop_id = b1.stop_id " +
      "and b.stop_sequence < b1.stop_sequence " +
      "and b.trip_id = c.trip_id and c.service_id = d.service_id and d.%s = 1 " +
      "and d.start_date <= '%s' and d.end_date >= '%s' and b.departure_time > '%s' " +
      "order by b.arrival_time";

    String fulFilledSelectQuery = String.format(selectQuery,
      fromId,
      toId,
      getDBWeekDayString(calendar),
      getDBDateString(calendar),
      getDBDateString(calendar),
      getDBTimeString(calendar));

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulFilledSelectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        Trip trip = new Trip();
        trip.setId(cursor.getString(0));
        trip.setFromStation(cursor.getString(1));
        trip.setToStation(cursor.getString(2));

        try {
          Calendar arrival = Calendar.getInstance();
          arrival.setTime(dbTimeFormat.parse(cursor.getString(4)));
          trip.setArrival(arrival);

          Calendar departure = Calendar.getInstance();
          departure.setTime(dbTimeFormat.parse(cursor.getString(3)));
          trip.setDeparture(departure);
        } catch (ParseException e) {

        }

        trips.add(trip);
      } while (cursor.moveToNext());
    }

    db.close();
    return trips;
  }

  public List<Station> getStops(Trip trip) {
    List<Station> stations = new ArrayList<Station>();
    if(trip == null) {
      return stations;
    }

    String selectQuery = "select b.stop_id, b.stop_name, b.stop_lat, b.stop_lon, b.zone_id " +
      "from stop_times a, stops b where a.stop_id = b.stop_id and a.trip_id='%s' and a.departure_time >= '%s' and a.arrival_time <= '%s'";

    String fulfilledSelectQuery = String.format(selectQuery,
      trip.getId(),
      getDBTimeString(trip.getDeparture()),
      getDBTimeString(trip.getArrival()));

    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.rawQuery(fulfilledSelectQuery, null);

    if (cursor.moveToFirst()) {
      do {
        Station station = new Station();
        station.setId(cursor.getString(0));
        station.setName(cursor.getString(1));
        Location l = new Location(Station.LOCATION_PROVIDER);
        l.setLatitude(Double.parseDouble(cursor.getString(2)));
        l.setLongitude(Double.parseDouble(cursor.getString(3)));
        station.setLocation(l);
        if(cursor.getString(4) != null) {
          station.setZone(Integer.parseInt(cursor.getString(4)));
        }
        stations.add(station);
      } while (cursor.moveToNext());
    }

    db.close();
    return stations;
  }
}
