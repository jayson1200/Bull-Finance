package com.example.bullfinance;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    public RequestQueue queue;

    String url = "https://financialmodelingprep.com//api/v3/majors-indexes/.DJI";
    ConstraintLayout theSplashScreen;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        queue = Volley.newRequestQueue(this);

        theSplashScreen = (ConstraintLayout) findViewById(R.id.activity_Main);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int theChangesObjVal = response.getInt("changes");

                    if (theChangesObjVal >= 0) {
                        Log.d(TAG, "value is above 0");
                        theSplashScreen.setBackgroundColor(Color.parseColor("#1b990b"));
                    } else if (theChangesObjVal < 0) {
                        Log.d(TAG, "value is below 0");
                        theSplashScreen.setBackgroundColor(Color.parseColor("#e33131"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        });

        queue.add(jsonObjectRequest);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, SearchAndWatch.class);
                startActivity(homeIntent);
                finish();
            }
        }, 4000);


    }
}


