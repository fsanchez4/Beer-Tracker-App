package com.csc4360.beertracker.Controller;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csc4360.beertracker.DatabaseModel.Beer;
import com.csc4360.beertracker.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private List<Beer> beers;
    private OnBeerListener onBeerListener;
    private String searchName;

    public SearchAdapter(Context context, List<Beer> beers, OnBeerListener onBeerListener) {
        this.context = context;
        this.beers = beers;
        this.onBeerListener = onBeerListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_list_search, parent, false);

        return new SearchViewHolder(itemView, onBeerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.beer.setText(beers.get(position).getName());
        searchName = beers.get(position).getName();
        holder.brewery.setText(beers.get(position).getBrewery());
        holder.beerType.setText(beers.get(position).getType());
        holder.aBv.setText(beers.get(position).getAbv());
        holder.beerRating.setRating(beers.get(position).getRating());

        if (isNumeric(beers.get(position).getBeerImage())) {
            holder.beerImg.setImageResource(Integer.valueOf(beers.get(position).getBeerImage()));
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(beers.get(position).getBeerImage());
            holder.beerImg.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//    public TextView beerName, breweryName, beerType, aBv;
//
//    public SearchViewHolder(@NonNull View itemView) {
//        super(itemView);
//        beerName = itemView.findViewById(R.id.beerName_textView);
//        breweryName = itemView.findViewById(R.id.breweryName_textView);
//        beerType = itemView.findViewById(R.id.beerType_textView);
//        aBv = itemView.findViewById(R.id.abv_textView);
//    }

        public TextView beer, brewery, beerType, aBv;
        public CircleImageView beerImg;
        public RatingBar beerRating;
        public OnBeerListener onBeerListener;

        public SearchViewHolder(@NonNull View itemView, OnBeerListener onBeerListener) {
            super(itemView);
            beer = itemView.findViewById(R.id.beerName_textView);
            brewery = itemView.findViewById(R.id.breweryName_textView);
            beerType = itemView.findViewById(R.id.beerType_textView);
            aBv = itemView.findViewById(R.id.abv_textView);
            beerRating = itemView.findViewById(R.id.ratingBar);
            beerImg = itemView.findViewById(R.id.testImage);

            this.onBeerListener = onBeerListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onBeerListener.onBeerClick(searchName);
        }
    }

    public interface OnBeerListener{
        void onBeerClick(String searchName);
    }

    public static boolean isNumeric(String strNum) {
        try {
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }


}


