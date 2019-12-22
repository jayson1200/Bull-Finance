package com.example.bullfinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.lang.Object;

public class SearchAndWatch extends AppCompatActivity {

    public EditText usrSearch =  (EditText) findViewById(R.id.searchEditText);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_watch);

        usrSearch.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus == false)
                {
                    usrSearch.setText("Ticker Symbol or Name");
                }

                if(hasFocus == true)
                {
                    usrSearch.setText("");
                }
            }
        });


    }
}
