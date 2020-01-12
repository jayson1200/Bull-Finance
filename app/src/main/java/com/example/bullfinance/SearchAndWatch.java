package com.example.bullfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


public class SearchAndWatch extends AppCompatActivity {

    public EditText usrSearch;
    public ConstraintLayout searchAndWatchConstlyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_watch);

        usrSearch = findViewById(R.id.searchEditText);
        searchAndWatchConstlyt = findViewById(R.id.activity_SearchandWatch);

        usrSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    usrSearch.setText("Ticker Symbol or Name");
                }

                if (hasFocus == true) {
                    usrSearch.setText("");
                }
            }
        });

        final Intent toStockInfo = new Intent(SearchAndWatch.this, stockinfo.class);

        usrSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    toStockInfo.putExtra("STOCKTICKER", usrSearch.getText().toString().toUpperCase());
                    startActivity(toStockInfo);
                    finish();
                }

                return false;
            }
        });

        searchAndWatchConstlyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrSearch.clearFocus();
                hideKeyboardFrom(getBaseContext(), usrSearch);
            }
        });

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

