package com.example.bullfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SearchAndWatch extends AppCompatActivity {

    public TextView searchViewBTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_watch);

        searchViewBTN = findViewById(R.id.ToSearchLayoutBtn);

        final Intent toSearch = new Intent(SearchAndWatch.this, Search.class);

        searchViewBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toSearch);
            }
        });


    }

}

