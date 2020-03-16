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
import android.widget.HorizontalScrollView;
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
import com.github.mikephil.charting.components.XAxis;
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
import java.util.Calendar;
import java.util.Set;

public class HomeFragment extends Fragment {


    public TextView thePrice;

    public TextView intervalText;

    public Bundle bundle;

    public String tickerSymbol;

    public String url;

    public String chartUrl;

    static public CandleStickChart theChart;

    Handler handler = new Handler();

    public Button todayBTN, fiveDayBTN, twoWeeksBTN, monthBTN, sixMonthBTN, lstQtrBTN, oneYearBTN, ytdBTN, allTimeBTN;

    public Button intOneDayBTN, intFiveDayBTN, intMonthBTN, intSixMonthBTN, intOneYearBTN;

    public String selectedTimeFrameBTN;

    public String selectedIntervalBTN;

    public HorizontalScrollView intervalScrollView, timeFrameScrollView;

    public static ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();

    //public ArrayList<CandleEntry> candleEntriesEdited = new ArrayList<CandleEntry>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        thePrice = (TextView) root.findViewById(R.id.priceText);

        intervalText = root.findViewById(R.id.intervalText);

        theChart = root.findViewById(R.id.chart);

        bundle = getActivity().getIntent().getExtras();

        tickerSymbol = bundle.getString("STOCKTICKER");

        url = "https://financialmodelingprep.com/api/v3/stock/real-time-price/" + tickerSymbol;

        intervalScrollView = root.findViewById(R.id.intervalScrollView);
        timeFrameScrollView = root.findViewById(R.id.timeScrollView);

        todayBTN = root.findViewById(R.id.todaybtn);
        fiveDayBTN = root.findViewById(R.id.fivedaybtn);
        twoWeeksBTN = root.findViewById(R.id.twoweeksbtn);
        monthBTN = root.findViewById(R.id.monthbtn);
        sixMonthBTN = root.findViewById(R.id.sixmonthbtn);
        lstQtrBTN = root.findViewById(R.id.lstqtrBtn);
        oneYearBTN = root.findViewById(R.id.oneyearbtn);
        ytdBTN = root.findViewById(R.id.ytdbtn);
        allTimeBTN = root.findViewById(R.id.alltimebtn);

        intOneDayBTN = root.findViewById(R.id.intonedaybtn);
        intFiveDayBTN = root.findViewById(R.id.intfivedaybtn);
        intMonthBTN = root.findViewById(R.id.intmonthbtn);
        intSixMonthBTN = root.findViewById(R.id.intsixmonthbtn);
        intOneYearBTN = root.findViewById(R.id.intoneyearbtn);
        
        todayBTN.setOnClickListener(TimeFrameBTNClickListener);
        fiveDayBTN.setOnClickListener(TimeFrameBTNClickListener);
        twoWeeksBTN.setOnClickListener(TimeFrameBTNClickListener);
        monthBTN.setOnClickListener(TimeFrameBTNClickListener);
        sixMonthBTN.setOnClickListener(TimeFrameBTNClickListener);
        lstQtrBTN.setOnClickListener(TimeFrameBTNClickListener);
        oneYearBTN.setOnClickListener(TimeFrameBTNClickListener);
        ytdBTN.setOnClickListener(TimeFrameBTNClickListener);
        allTimeBTN.setOnClickListener(TimeFrameBTNClickListener);

        intOneDayBTN.setOnClickListener(IntervalBTNClickListener);
        intFiveDayBTN.setOnClickListener(IntervalBTNClickListener);
        intMonthBTN.setOnClickListener(IntervalBTNClickListener);
        intSixMonthBTN.setOnClickListener(IntervalBTNClickListener);
        intOneYearBTN.setOnClickListener(IntervalBTNClickListener);


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

        if(!candleEntries.isEmpty())
        {
            MakeCandleStockChart(candleEntries);
        }


        intervalScrollView.setVisibility(View.INVISIBLE);
        intervalText.setVisibility(View.INVISIBLE);

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

    public void MakeCandleStockChart(ArrayList<CandleEntry> theCandleEntries) {

        CandleDataSet chartSet = new CandleDataSet(theCandleEntries, tickerSymbol);

        chartSet.setDrawIcons(false);
        chartSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        chartSet.setShadowColor(Color.BLACK);
        chartSet.setShadowWidth(0.7f);
        chartSet.setDecreasingColor(Color.RED);
        chartSet.setDecreasingPaintStyle(Paint.Style.FILL);
        chartSet.setIncreasingColor(Color.rgb(122, 242, 84));
        chartSet.setValueTextSize(10f);
        chartSet.setIncreasingPaintStyle(Paint.Style.FILL);
        chartSet.setNeutralColor(Color.BLUE);
        chartSet.setHighlightEnabled(true);
        chartSet.setHighLightColor(Color.BLACK);

        XAxis xAxis = theChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        CandleData data = new CandleData(chartSet);

        theChart.setData(data);

        theChart.invalidate();
    }

    public View.OnClickListener TimeFrameBTNClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            Button theSelectedButton =  (Button) v;

            intervalScrollView.setVisibility(View.VISIBLE);
            intervalText.setVisibility(View.VISIBLE);

            selectedIntervalBTN = "";

            intOneDayBTN.setVisibility(View.VISIBLE);
            intFiveDayBTN.setVisibility(View.VISIBLE);
            intMonthBTN.setVisibility(View.VISIBLE);
            intSixMonthBTN.setVisibility(View.VISIBLE);
            intOneYearBTN.setVisibility(View.VISIBLE);

            todayBTN.setTextColor(Color.BLACK);
            fiveDayBTN.setTextColor(Color.BLACK);
            twoWeeksBTN.setTextColor(Color.BLACK);
            monthBTN.setTextColor(Color.BLACK);
            sixMonthBTN.setTextColor(Color.BLACK);
            lstQtrBTN.setTextColor(Color.BLACK);
            oneYearBTN.setTextColor(Color.BLACK);
            ytdBTN.setTextColor(Color.BLACK);
            allTimeBTN.setTextColor(Color.BLACK);

            intOneDayBTN.setTextColor(Color.BLACK);
            intFiveDayBTN.setTextColor(Color.BLACK);
            intMonthBTN.setTextColor(Color.BLACK);
            intSixMonthBTN.setTextColor(Color.BLACK);
            intOneYearBTN.setTextColor(Color.BLACK);

            switch(v.getId())
            {
                case R.id.todaybtn:
                    selectedTimeFrameBTN = "today";

                    intervalScrollView.setVisibility(View.INVISIBLE);
                    intervalText.setVisibility(View.INVISIBLE);

                    todayBTN.setTextColor(Color.WHITE);

                    intOneDayBTN.setVisibility(View.GONE);
                    intFiveDayBTN.setVisibility(View.GONE);
                    intMonthBTN.setVisibility(View.GONE);
                    intSixMonthBTN.setVisibility(View.GONE);
                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.fivedaybtn:
                    selectedTimeFrameBTN = "fiveday";

                    fiveDayBTN.setTextColor(Color.WHITE);

                    intFiveDayBTN.setVisibility(View.GONE);
                    intMonthBTN.setVisibility(View.GONE);
                    intSixMonthBTN.setVisibility(View.GONE);
                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.twoweeksbtn:
                    selectedTimeFrameBTN = "twoweek";

                    twoWeeksBTN.setTextColor(Color.WHITE);

                    intMonthBTN.setVisibility(View.GONE);
                    intSixMonthBTN.setVisibility(View.GONE);
                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.monthbtn:
                    selectedTimeFrameBTN = "month";

                    monthBTN.setTextColor(Color.WHITE);

                    intMonthBTN.setVisibility(View.GONE);
                    intSixMonthBTN.setVisibility(View.GONE);
                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.sixmonthbtn:
                    selectedTimeFrameBTN = "sixmonth";

                    sixMonthBTN.setTextColor(Color.WHITE);

                    intSixMonthBTN.setVisibility(View.GONE);
                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.lstqtrBtn:
                    selectedTimeFrameBTN = "lastquarter";

                    lstQtrBTN.setTextColor(Color.WHITE);

                    intSixMonthBTN.setVisibility(View.GONE);
                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.oneyearbtn:
                    selectedTimeFrameBTN = "oneyear";

                    oneYearBTN.setTextColor(Color.WHITE);

                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.ytdbtn:
                    selectedTimeFrameBTN = "ytd";

                    ytdBTN.setTextColor(Color.WHITE);

                    intOneYearBTN.setVisibility(View.GONE);
                    break;

                case R.id.alltimebtn:
                    selectedTimeFrameBTN = "alltime";

                    allTimeBTN.setTextColor(Color.WHITE);
                    break;
            }
        }
    };

    public View.OnClickListener IntervalBTNClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {

            Button theSelectedButton =  (Button) v;

            intOneDayBTN.setTextColor(Color.BLACK);
            intFiveDayBTN.setTextColor(Color.BLACK);
            intMonthBTN.setTextColor(Color.BLACK);
            intSixMonthBTN.setTextColor(Color.BLACK);
            intOneYearBTN.setTextColor(Color.BLACK);

            switch(v.getId())
            {
                case R.id.intonedaybtn:
                    selectedIntervalBTN = "intoneday";

                    intOneDayBTN.setTextColor(Color.WHITE);


                    break;

                case R.id.intfivedaybtn:
                    selectedIntervalBTN = "intfiveday";

                    intFiveDayBTN.setTextColor(Color.WHITE);
                    break;

                case R.id.intmonthbtn:
                    selectedIntervalBTN = "intmonth";

                    intMonthBTN.setTextColor(Color.WHITE);
                    break;

                case R.id.intsixmonthbtn:
                    selectedIntervalBTN = "intsixmonth";

                    intSixMonthBTN.setTextColor(Color.WHITE);
                    break;

                case R.id.intoneyearbtn:
                    selectedIntervalBTN = "intoneyear";

                    intOneYearBTN.setTextColor(Color.WHITE);
                    break;
            }

            MakeCandleStockChart(configureCandleData(selectedTimeFrameBTN, selectedIntervalBTN, candleEntries));
        }
    };

    public ArrayList<CandleEntry> configureCandleData(String timeFrame, String interval, ArrayList<CandleEntry> uneditedCandleData)
    {
        ArrayList<CandleEntry> editedCandleData = uneditedCandleData;

        switch(timeFrame)
        {
            case "fiveday":

                if(uneditedCandleData.size() < 6)
                {
                    break;
                }

                editedCandleData =  new ArrayList<CandleEntry>(uneditedCandleData.subList(uneditedCandleData.size() -6, uneditedCandleData.size() -1));
                break;

            case "twoweek":

                if(uneditedCandleData.size() < 15)
                {
                    break;
                }

                editedCandleData = new ArrayList<CandleEntry>(uneditedCandleData.subList(uneditedCandleData.size() -15, uneditedCandleData.size() -1));
                break;

            case "month":

                if(uneditedCandleData.size() < 31)
                {
                    break;
                }

                editedCandleData = new ArrayList<CandleEntry>(uneditedCandleData.subList(uneditedCandleData.size() -31, uneditedCandleData.size() -1));
                break;

            case "sixmonth":

                if(uneditedCandleData.size() < 181)
                {
                    break;
                }

                editedCandleData =  new ArrayList<CandleEntry>(uneditedCandleData.subList(uneditedCandleData.size() -181, uneditedCandleData.size() -1));
                break;

            case "lastquarter":
                //Save for later implementation
                try
                {
                    throw new ItsNoteEvenFinishedException("The Last quarter time frame is on company by company basis");
                }
                catch(ItsNoteEvenFinishedException exception)
                {
                    Log.w("HomeFragment", "Finish it or stop clicking on it");
                }
                break;

            case "oneyear":

                if(uneditedCandleData.size() < HowManyDaysPassed())
                {
                    break;
                }

                editedCandleData = new ArrayList<CandleEntry>(uneditedCandleData.subList(uneditedCandleData.size() -HowManyDaysPassed()-1, uneditedCandleData.size() -1));
                break;

            case "ytd":
                if(uneditedCandleData.size() < 361)
                {
                    break;
                }

                editedCandleData = new ArrayList<CandleEntry>(uneditedCandleData.subList(uneditedCandleData.size() -361, uneditedCandleData.size() -1));
                break;

            case "alltime":
                break;
        }

        ArrayList<CandleEntry> intervalNewArray = new ArrayList<CandleEntry>();

        int lastIndex;

        float theHigh = 0, theLow = Float.MAX_VALUE;

        switch(interval)
        {
            case "intoneday":
                //Dont really do anything as it starts like this
                break;
            case "intfiveday":

                lastIndex = 0;

                for(int i = 0; i + 5 < editedCandleData.size(); i+=5)
                {
                    lastIndex++;

                    theHigh = 0;
                    theLow = Float.MAX_VALUE;

                    for(int j = i; j < i+5; j++)
                    {
                        if(editedCandleData.get(j).getHigh() > theHigh)
                        {
                            theHigh = editedCandleData.get(j).getHigh();
                        }

                        if(editedCandleData.get(j).getLow() < theLow)
                        {
                            theLow = editedCandleData.get(j).getLow();
                        }
                    }

                    intervalNewArray.add(new CandleEntry(i, theHigh, theLow,  editedCandleData.get(i).getOpen(), editedCandleData.get(i+5).getClose()));
                }

                theHigh = 0;
                theLow = Float.MAX_VALUE;

                for(int i = lastIndex; i < editedCandleData.size(); i++)
                {
                    if(editedCandleData.get(i).getHigh() > theHigh)
                    {
                        theHigh = editedCandleData.get(i).getHigh();
                    }

                    if(editedCandleData.get(i).getLow() < theLow)
                    {
                        theLow = editedCandleData.get(i).getLow();
                    }
                }

                intervalNewArray.add(new CandleEntry(intervalNewArray.size(), theHigh, theLow,  editedCandleData.get(lastIndex).getOpen(), editedCandleData.get(editedCandleData.size() -1).getClose()));

                editedCandleData = intervalNewArray;
                break;
            case "intmonth":
                
                lastIndex = 0;

                for(int i = 0; i + 30 < editedCandleData.size(); i+=30)
                {
                    lastIndex++;

                    theHigh = 0;
                    theLow = Float.MAX_VALUE;

                    for(int j = i; j < i+30; j++)
                    {
                        if(editedCandleData.get(j).getHigh() > theHigh)
                        {
                            theHigh = editedCandleData.get(j).getHigh();
                        }

                        if(editedCandleData.get(j).getLow() < theLow)
                        {
                            theLow = editedCandleData.get(j).getLow();
                        }
                    }

                    intervalNewArray.add(new CandleEntry(i, theHigh, theLow,  editedCandleData.get(i).getOpen(), editedCandleData.get(i+30).getClose()));
                }

                theHigh = 0;
                theLow = Float.MAX_VALUE;

                for(int i = lastIndex; i < editedCandleData.size(); i++)
                {
                    if(editedCandleData.get(i).getHigh() > theHigh)
                    {
                        theHigh = editedCandleData.get(i).getHigh();
                    }

                    if(editedCandleData.get(i).getLow() < theLow)
                    {
                        theLow = editedCandleData.get(i).getLow();
                    }
                }

                intervalNewArray.add(new CandleEntry(intervalNewArray.size(), theHigh, theLow,  editedCandleData.get(lastIndex).getOpen(), editedCandleData.get(editedCandleData.size() -1).getClose()));

                editedCandleData = intervalNewArray;
                break;
            case "intsixmonth":

                lastIndex = 0;

                for(int i = 0; i + 180 < editedCandleData.size(); i+=180)
                {
                    lastIndex++;

                    theHigh = 0;
                    theLow = Float.MAX_VALUE;

                    for(int j = i; j < i+180; j++)
                    {
                        if(editedCandleData.get(j).getHigh() > theHigh)
                        {
                            theHigh = editedCandleData.get(j).getHigh();
                        }

                        if(editedCandleData.get(j).getLow() < theLow)
                        {
                            theLow = editedCandleData.get(j).getLow();
                        }
                    }

                    intervalNewArray.add(new CandleEntry(i, theHigh, theLow,  editedCandleData.get(i).getOpen(), editedCandleData.get(i+180).getClose()));
                }

                theHigh = 0;
                theLow = Float.MAX_VALUE;

                for(int i = lastIndex; i < editedCandleData.size(); i++)
                {
                    if(editedCandleData.get(i).getHigh() > theHigh)
                    {
                        theHigh = editedCandleData.get(i).getHigh();
                    }

                    if(editedCandleData.get(i).getLow() < theLow)
                    {
                        theLow = editedCandleData.get(i).getLow();
                    }
                }

                intervalNewArray.add(new CandleEntry(intervalNewArray.size(), theHigh, theLow,  editedCandleData.get(lastIndex).getOpen(), editedCandleData.get(editedCandleData.size() -1).getClose()));

                editedCandleData = intervalNewArray;
                
                break;
            case "intoneyear":

                lastIndex = 0;

                for(int i = 0; i + 360 < editedCandleData.size(); i+=360)
                {
                    lastIndex++;

                    theHigh = 0;
                    theLow = Float.MAX_VALUE;

                    for(int j = i; j < i+360; j++)
                    {
                        if(editedCandleData.get(j).getHigh() > theHigh)
                        {
                            theHigh = editedCandleData.get(j).getHigh();
                        }

                        if(editedCandleData.get(j).getLow() < theLow)
                        {
                            theLow = editedCandleData.get(j).getLow();
                        }
                    }

                    intervalNewArray.add(new CandleEntry(i, theHigh, theLow,  editedCandleData.get(i).getOpen(), editedCandleData.get(i+360).getClose()));
                }

                theHigh = 0;
                theLow = Float.MAX_VALUE;

                for(int i = lastIndex; i < editedCandleData.size(); i++)
                {
                    if(editedCandleData.get(i).getHigh() > theHigh)
                    {
                        theHigh = editedCandleData.get(i).getHigh();
                    }

                    if(editedCandleData.get(i).getLow() < theLow)
                    {
                        theLow = editedCandleData.get(i).getLow();
                    }
                }

                intervalNewArray.add(new CandleEntry(intervalNewArray.size(), theHigh, theLow,  editedCandleData.get(lastIndex).getOpen(), editedCandleData.get(editedCandleData.size() -1).getClose()));

                editedCandleData = intervalNewArray;
                break;
        }

        for(int i = 0; i < editedCandleData.size(); i++)
        {
            editedCandleData.get(i).setX(i);
        }

        return editedCandleData;
    }


    class ItsNoteEvenFinishedException extends Exception
    {
        public ItsNoteEvenFinishedException(String s)
        {
            super(s);
        }
    }

    public int HowManyDaysPassed()
    {
        Calendar calendar = Calendar.getInstance();

        Log.w("HomeFragment", calendar.get(Calendar.DAY_OF_YEAR) + "");

        return calendar.get(Calendar.DAY_OF_YEAR);
    }



}

