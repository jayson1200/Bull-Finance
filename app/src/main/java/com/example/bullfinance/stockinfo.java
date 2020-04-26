package com.example.bullfinance;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bullfinance.ui.home.HomeFragment;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class stockinfo extends AppCompatActivity {

    public String url = "";
    public Bundle bundle;
    public String tickerSymbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockinfo);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_News, R.id.navigation_PatternIndicators, R.id.navigation_SpecialStats)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        bundle = getIntent().getExtras();
        tickerSymbol = bundle.getString("STOCKTICKER");

        setToolBarTitle();
        PutChartData();
    }

    public void setToolBarTitle()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar theToolbar = getSupportActionBar();

        theToolbar.setDisplayHomeAsUpEnabled(true);

        url = "https://financialmodelingprep.com/api/v3/company/profile/" + this.getIntent().getExtras().getString("STOCKTICKER");

        JsonObjectRequest toolBarTitleRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    ActionBar nameRequestToolbar = getSupportActionBar();

                    JSONObject myJsonObject = response.getJSONObject("profile");

                    nameRequestToolbar.setTitle(myJsonObject.getString("companyName"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        NetworkBridge.getInstance(this).addToRequestQueue(toolBarTitleRequest);
    }

    public void PutChartData()
    {
        String chartUrl = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ this.getIntent().getExtras().getString("STOCKTICKER") + "?from=0000-00-00&to=" + GetCurrentDate();

        Log.w("StockInfoActivity", chartUrl);

        JsonObjectRequest chartDataRequest = new JsonObjectRequest(Request.Method.GET, chartUrl, null, new Response.Listener<JSONObject>() {

            ArrayList<CandleEntry> candleData = new ArrayList<CandleEntry>();

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray historicalArray = response.getJSONArray("historical");

                    for(int i = 0; i  < historicalArray.length(); i++)
                    {
                        candleData.add(new CandleEntry(i, historicalArray.getJSONObject(i).getInt("high"), historicalArray.getJSONObject(i).getInt("low"), historicalArray.getJSONObject(i).getInt("open"), historicalArray.getJSONObject(i).getInt("close")));
                    }

                    HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments().get(0);
                    homeFragment.candleEntries = candleData;

                    homeFragment.MakeCandleStockChart(candleData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        NetworkBridge.getInstance(this).addToRequestQueue(chartDataRequest);
    }

    public String GetCurrentDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(calendar.getTime());
    }
}
