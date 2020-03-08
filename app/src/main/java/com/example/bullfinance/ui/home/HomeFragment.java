package com.example.bullfinance.ui.home;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
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
import com.example.bullfinance.stockinfo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarLineScatterCandleBubbleDataProvider;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class HomeFragment extends Fragment {


    public TextView thePrice;

    public Bundle bundle;

    public String tickerSymbol;

    public String url;

    public String chartUrl;

    static public CandleStickChart theChart;

    Handler handler = new Handler();

    public Button todayBTN, fiveDayBTN, twoWeeksBTN, monthBTN, sixMonthBTN, lstQtrBTN, oneYearBTN, ytdBtn, allTimeBTN;

    public Button intOneDayBTN, intFiveDayBTN, intMonthBTN, intSixMonthBTN, intOneYearBTN;

    public Button selectedTimeFrameBTN;

    public Button selectedIntervalBTN;

    public ScrollView intervalScrollView;

    public ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        thePrice = (TextView) root.findViewById(R.id.priceText);

        theChart = root.findViewById(R.id.chart);

        bundle = getActivity().getIntent().getExtras();

        tickerSymbol = bundle.getString("STOCKTICKER");

        url = "https://financialmodelingprep.com/api/v3/stock/real-time-price/" + tickerSymbol;

        todayBTN = root.findViewById(R.id.todaybtn);
        fiveDayBTN = root.findViewById(R.id.fivedaybtn);
        twoWeeksBTN = root.findViewById(R.id.monthbtn);
        monthBTN = root.findViewById(R.id.monthbtn);
        sixMonthBTN = root.findViewById(R.id.sixmonthbtn);
        lstQtrBTN = root.findViewById(R.id.lstqtrBtn);
        oneYearBTN = root.findViewById(R.id.oneyearbtn);
        ytdBtn = root.findViewById(R.id.ytdbtn);
        allTimeBTN = root.findViewById(R.id.alltimebtn);


        intOneDayBTN = root.findViewById(R.id.intonedaybtn);
        intFiveDayBTN = root.findViewById(R.id.intfivedaybtn);
        intMonthBTN = root.findViewById(R.id.intmonthbtn);
        intSixMonthBTN = root.findViewById(R.id.intsixmonthbtn);
        intOneYearBTN = root.findViewById(R.id.intoneyearbtn);


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

    public void MakeCandleStockChart() {

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

        theChart.invalidate();

    }

}

