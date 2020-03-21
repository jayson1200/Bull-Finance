package com.example.bullfinance;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Search extends AppCompatActivity {

    public EditText searchEditText;

    public TextView searchTextView;

    public String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText= findViewById(R.id.searchEditText2);

        searchTextView = findViewById(R.id.searchTextView);

        searchTextView.setMovementMethod(new ScrollingMovementMethod());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                url = "https://financialmodelingprep.com/api/v3/search?query=" + searchEditText.getText().toString() + "&limit=10";

                NetworkBridge.getInstance(getBaseContext()).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        ArrayList<SearchEntryInfo> searchedItemInfo = new ArrayList<SearchEntryInfo>();

                        ArrayList<Button> buttons = new ArrayList<Button>();

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                searchedItemInfo.add(new SearchEntryInfo(response.getJSONObject(i).getString("symbol"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("currency"), response.getJSONObject(i).getString("stockExchange"), response.getJSONObject(i).getString("exchangeShortName")));
                            }

                            searchTextView.setText("");

                            for(int i = 0; i < searchedItemInfo.size(); i++)
                            {
                                searchTextView.append(searchedItemInfo.get(i).getSymbol() + "\n" + searchedItemInfo.get(i).getName() + "\n" + searchedItemInfo.get(i).getCurrency() + "\n" + searchedItemInfo.get(i).getStockExchange() + "\n" + searchedItemInfo.get(i).getExchangeShortName() + "\n\n");
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
                }));
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
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