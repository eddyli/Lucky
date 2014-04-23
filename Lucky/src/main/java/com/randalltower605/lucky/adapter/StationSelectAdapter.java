package com.randalltower605.lucky.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import com.randalltower605.lucky.model.Station;

import java.util.List;

import com.randalltower605.lucky.util.FontUtil;

/**
 * Created by eli on 3/16/14.
 */
public class StationSelectAdapter extends ArrayAdapter<Station>{
    public static final int STATION_ID_KEY = '1';
    public StationSelectAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public StationSelectAdapter(Context context, int layoutResourceId, int textViewResourceId, List<Station> data) {
        super(context, layoutResourceId, textViewResourceId, data);
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);

        Typeface myFont = FontUtil.getTypeFace(getContext().getAssets(), FontUtil.GOTHAM_NARROW_LIGHT);
        if(myFont != null)
        {
            v.setTypeface(myFont);
        }
        Station station = getItem(position);
        v.setTag(station);
        return v;
    }

    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        Typeface myFont = FontUtil.getTypeFace(getContext().getAssets(), FontUtil.GOTHAM_NARROW_LIGHT);
        if(myFont != null)
        {
            v.setTypeface(myFont);
        }
        return v;
    }
}
