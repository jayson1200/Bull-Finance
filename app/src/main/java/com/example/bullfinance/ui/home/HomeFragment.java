package com.example.bullfinance.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bullfinance.NetworkBridge;
import com.example.bullfinance.R;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {


    public TextView thePrice;

    public Bundle bundle;

    public String tickerSymbol;

    public String url;

    Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        thePrice = (TextView) root.findViewById(R.id.priceText);

        bundle = getActivity().getIntent().getExtras();

        tickerSymbol = bundle.getString("STOCKTICKER");

        url = "https://financialmodelingprep.com/api/v3/stock/real-time-price/" + tickerSymbol;

        setStockPrice();
        handler.postDelayed(periodicUpdate, 5 * 1000);

        return root;
    }


    public void setStockPrice() {


        JsonObjectRequest getStockPriceRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Double thePriceObt;

                    thePriceObt = response.getDouble("price");

                    thePrice.setText(thePriceObt.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              
            }
        });

        NetworkBridge.getInstance(getContext()).addToRequestQueue(getStockPriceRequest);
    }

    Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 4 * 1000);

            setStockPrice();
        }
    };

}