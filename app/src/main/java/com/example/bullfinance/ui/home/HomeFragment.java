package com.example.bullfinance.ui.home;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class HomeFragment extends Fragment{


    public TextView thePrice;

    public Bundle bundle;

    public String tickerSymbol;

    public String url;

    public String chartUrl;

    static public CandleStickChart theChart;



    Handler handler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        thePrice = (TextView) root.findViewById(R.id.priceText);

        theChart = root.findViewById(R.id.chart);

        bundle = getActivity().getIntent().getExtras();

        tickerSymbol = bundle.getString("STOCKTICKER");

        url = "https://financialmodelingprep.com/api/v3/stock/real-time-price/" + tickerSymbol;

        chartUrl = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ tickerSymbol + "?from=2018-12-30&to=2020-02-02"; //<<<<This last part should become the time period in which the chart is going for

        SetStockPrice();
        handler.postDelayed(periodicUpdate, 5 * 1000);

        theChart.setTouchEnabled(true);
        theChart.setDragEnabled(true);
        theChart.setScaleEnabled(true);
        theChart.setPinchZoom(true);
        theChart.setDoubleTapToZoomEnabled(true);
        theChart.getDescription().setEnabled(false);
        theChart.setHighlightPerDragEnabled(true);

        theChart.animateXY(3000, 3000);

        MakeCandleStockChart();

        return root;
    }


    public void SetStockPrice() {


        JsonObjectRequest getStockPriceRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            Double thePriceObt;

            @Override
            public void onResponse(JSONObject response) {
                try {
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

            SetStockPrice();
        }
    };

    public void MakeCandleStockChart()
    {
        JsonObjectRequest getStockPriceRequest = new JsonObjectRequest(Request.Method.GET, chartUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();

                    JSONArray histroicalArray = response.getJSONArray("historical");

                    for(int i = 0; i  < histroicalArray.length(); i++)
                    {
                        candleEntries.add(new CandleEntry(i, histroicalArray.getJSONObject(i).getInt("high"), histroicalArray.getJSONObject(i).getInt("low"), histroicalArray.getJSONObject(i).getInt("open"), histroicalArray.getJSONObject(i).getInt("close")));
                    }

                    CandleDataSet chartSet = new CandleDataSet(candleEntries, tickerSymbol);

                    chartSet.setDrawIcons(false);
                    chartSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    chartSet.setShadowColor(Color.BLACK);
                    chartSet.setShadowWidth(0.7f);
                    chartSet.setDecreasingColor(Color.RED);
                    chartSet.setDecreasingPaintStyle(Paint.Style.FILL);
                    chartSet.setIncreasingColor(Color.rgb(122, 242, 84));
                    chartSet.setIncreasingPaintStyle(Paint.Style.FILL);
                    chartSet.setNeutralColor(Color.BLUE);
                    chartSet.setHighlightEnabled(true);
                    chartSet.setHighLightColor(Color.BLACK);

                    CandleData data = new CandleData(chartSet);

                    theChart.setData(data);
                    theChart.highlightValue(theChart.getHighlighter().getHighlight(theChart.getWidth(), 0));

                    theChart.invalidate();

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

}

