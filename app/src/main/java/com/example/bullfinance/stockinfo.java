package com.example.bullfinance;

import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;

import org.json.JSONException;
import org.json.JSONObject;

public class stockinfo extends AppCompatActivity {

    public String url = "";
    public Bundle bundle;

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

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar theToolbar = getSupportActionBar();

        theToolbar.setDisplayHomeAsUpEnabled(true);

        url = "https://financialmodelingprep.com/api/v3/company/profile/" + this.getIntent().getExtras().getString("STOCKTICKER");

        JsonObjectRequest getStockPriceRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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

        NetworkBridge.getInstance(this).addToRequestQueue(getStockPriceRequest);

    }

}
