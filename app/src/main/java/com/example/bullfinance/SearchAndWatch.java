package com.example.bullfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.Object;

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

