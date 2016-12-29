package com.yyy.xxx.mygalacticon;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by len on 2016. 12. 28..
 */

public class PhotoRequester  {

    public interface PhotoRequesterResponse {
        void receivedNewPhoto(Photo newPhoto);
    }

    private Calendar mCalendar;
    private SimpleDateFormat mDateFormat;
    private PhotoRequesterResponse mResponseListener;
    private Context mContext;
//    private OkHttpClient mClient;
    private JsonObjectRequest photoJson;
    private static final String BASE_URL = "https://api.nasa.gov/planetary/apod?";
    private static final String DATE_PARAMETER = "date=";
    private static final String API_KEY_PARAMETER = "&api_key=";
    private static final String MEDIA_TYPE_KEY = "media_type";
    private static final String MEDIA_TYPE_VIDEO_VALUE = "video";
    private boolean mLoadingData;

    public boolean isLoadingData() {
        return mLoadingData;
    }

    public PhotoRequester(Context context, PhotoRequesterResponse photoRequesterResponse) {

        mCalendar = Calendar.getInstance();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mResponseListener = photoRequesterResponse;

        mContext = context;
        mLoadingData = false;
    }
    public void getPhoto(Context context) throws IOException{

        RequestQueue requestQueue = MyVolley.getInstence(context).getRequestQueue();
        String date = mDateFormat.format(mCalendar.getTime());

        String urlRequest = BASE_URL + DATE_PARAMETER + date + API_KEY_PARAMETER +
                mContext.getString(R.string.api_key);

        photoJson = new JsonObjectRequest(Request.Method.GET
                        ,urlRequest
                        ,new JSONObject()
                ,networkSuccessListener(context)
                ,networkErrorListener(context));

        requestQueue.add(photoJson);

    }

    private Response.ErrorListener networkErrorListener(final Context context) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Network error",Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Response.Listener<JSONObject> networkSuccessListener(final Context context) {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                String from_server = null;

                try {

                    mCalendar.add(Calendar.DAY_OF_YEAR, -1);

                    if (!jsonObject.getString(MEDIA_TYPE_KEY).equals(MEDIA_TYPE_VIDEO_VALUE)) {
                        Photo receivePhoto = new Photo(jsonObject);
                        mResponseListener.receivedNewPhoto(receivePhoto);

                        mLoadingData = false;
                    } else {
                        getPhoto(context);
                    }

                } catch (JSONException e) {
                    mLoadingData = false;
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };


    }


}


