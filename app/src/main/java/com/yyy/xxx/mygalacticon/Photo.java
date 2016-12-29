package com.yyy.xxx.mygalacticon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by len on 2016. 12. 28..
 */

public class Photo implements Serializable {

    private static final long serialVersionUID = -5054928818830525643L;

    private String mDate;
    private String mHumanDate;
    private String mExplanation;
    private String mUrl;

    public Photo(JSONObject PhotoJSON) throws JSONException {

        mDate = PhotoJSON.getString("date");
        mExplanation = PhotoJSON.getString("explanation");
        mHumanDate = convertDateToHumanDate();
        mUrl = PhotoJSON.getString("url");
    }

    public String getmHumanDate() {
        return mHumanDate;
    }

    public String getmExplanation() {
        return mExplanation;
    }

    public String getmUrl() {
        return mUrl;
    }

    private String convertDateToHumanDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat humanDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        try {
            Date date = dateFormat.parse(mDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return humanDateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
