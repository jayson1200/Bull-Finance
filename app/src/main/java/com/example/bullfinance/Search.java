package com.example.bullfinance;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Search extends AppCompatActivity {

    public String url = "";

    private ConstraintLayout searchLayout;

    private RecyclerView tickerBtnRecyclerView;

    public Typeface font;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        font = ResourcesCompat.getFont(getApplicationContext(), R.font.signika_negative_bold_ttf_file);

        searchLayout = findViewById(R.id.searchlayout);

        tickerBtnRecyclerView = findViewById(R.id.tickerBtnRecyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBar theToolbar = getSupportActionBar();

        theToolbar.setTitle("");

        theToolbar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tickersearch_menu, menu);

        SearchView tickerSearchView = (SearchView) menu.findItem(R.id.ticker_searchview).getActionView();

        TextView tickerSearchViewTextView = tickerSearchView.findViewById(androidx.appcompat.R.id.search_src_text);

        tickerSearchView.setQueryHint("Ticker Symbol or Name");

        tickerSearchView.setIconifiedByDefault(false);

        tickerSearchView.setMaxWidth(10000);

        Log.w("Search Class: ", tickerSearchView.getWidth() + "");

        tickerSearchViewTextView.setTypeface(font);


        tickerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            final Intent toStockInfo = new Intent(Search.this, stockinfo.class);

            @Override
            public boolean onQueryTextSubmit(String query) {

                toStockInfo.putExtra("STOCKTICKER", query.toUpperCase());
                startActivity(toStockInfo);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newSearchUpdateRecyclerContent(newText.toUpperCase());

                return true;
            }
        });

        return true;
    }

    private void newSearchUpdateRecyclerContent(String search_txt)
    {
        url = "https://financialmodelingprep.com/api/v3/search?query=" + search_txt + "&limit=20";

        NetworkBridge.getInstance(getBaseContext()).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            TickerSuggestionAdapter suggestions;

            @Override
            public void onResponse(JSONArray response) {

                ArrayList<SearchEntryInfo> searchedItemInfo = new ArrayList<>();
                ArrayList<String> stockTickers = new ArrayList<>();
                ArrayList<String> companyNames = new ArrayList<>();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        searchedItemInfo.add(new SearchEntryInfo(response.getJSONObject(i).getString("symbol"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("currency"), response.getJSONObject(i).getString("stockExchange"), response.getJSONObject(i).getString("exchangeShortName")));
                    }

                    for(int i = 0; i < searchedItemInfo.size(); i++)
                    {
                        stockTickers.add(searchedItemInfo.get(i).getSymbol());
                        companyNames.add(searchedItemInfo.get(i).getName());
                    }

                    suggestions = new TickerSuggestionAdapter(getApplicationContext(), stockTickers.toArray(new String[searchedItemInfo.size()]), companyNames.toArray(new String[searchedItemInfo.size()]));

                    tickerBtnRecyclerView.setAdapter(suggestions);
                    tickerBtnRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        }));
    }
}

class SearchEntryInfo {
    private String symbol, name, currency, stockExchange, exchangeShortName;

    public SearchEntryInfo(String mSymbol, String mName, String mCurrency, String mStockExchange, String mExchangeShortName) {
        symbol = mSymbol;
        name = mName;
        currency = mCurrency;
        stockExchange = mStockExchange;
        exchangeShortName = mExchangeShortName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public String getExchangeShortName() {
        return exchangeShortName;
    }
}

