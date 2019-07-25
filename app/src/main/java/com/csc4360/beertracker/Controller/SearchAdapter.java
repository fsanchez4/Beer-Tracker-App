package com.csc4360.beertracker.Controller;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csc4360.beertracker.DatabaseModel.Beer;
import com.csc4360.beertracker.R;

import java.util.List;

class SearchViewHolder extends RecyclerView.ViewHolder {


    public TextView beerName, breweryName, beerType, aBv;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        beerName = itemView.findViewById(R.id.beerName_textView);
        breweryName = itemView.findViewById(R.id.breweryName_textView);
        beerType = itemView.findViewById(R.id.beerType_textView);
        aBv = itemView.findViewById(R.id.abv_textView);
    }

}


public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Context context;
    private List<Beer> beers;

    public SearchAdapter(Context context, List<Beer> beers) {
        this.context = context;
        this.beers = beers;
    }

    @NonNull
    @Override

    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_list_search, parent, false);

        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.beerName.setText(beers.get(position).getName());
        holder.breweryName.setText(beers.get(position).getBrewery());
        holder.beerType.setText(beers.get(position).getType());
        holder.aBv.setText(beers.get(position).getAbv());

    }

    @Override
    public int getItemCount() {
        return beers.size();
    }
}
