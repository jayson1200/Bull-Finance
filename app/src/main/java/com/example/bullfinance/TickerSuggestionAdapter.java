package com.example.bullfinance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TickerSuggestionAdapter extends RecyclerView.Adapter<TickerSuggestionAdapter.MyViewHolder>
{
    String[] tickerSymbols;
    String[] companyNames;
    Context context;

    public TickerSuggestionAdapter(Context ct, String[] mTickerSymbols, String[] mCompanyNames)
    {
        tickerSymbols = mTickerSymbols;
        companyNames = mCompanyNames;
        context = ct;
    }

    public TickerSuggestionAdapter(Context ct)
    {
        tickerSymbols = new String[0];
        companyNames = new String[0];
        context = ct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.tickersuggestionitem, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.ticker_txt.setText(tickerSymbols[position]);
        holder.companyName_txt.setText(companyNames[position]);

        holder.sugItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, stockinfo.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("STOCKTICKER",  tickerSymbols[position].toUpperCase());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickerSymbols.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView ticker_txt, companyName_txt;
        ConstraintLayout sugItemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ticker_txt = itemView.findViewById(R.id.tickerSymbol_txt);
            companyName_txt = itemView.findViewById(R.id.companyName_txt);
            sugItemLayout = itemView.findViewById(R.id.itmSugLayout);
        }
    }
}
