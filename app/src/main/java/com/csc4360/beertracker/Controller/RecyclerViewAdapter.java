package com.csc4360.beertracker.Controller;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csc4360.beertracker.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> beerNames;
    private ArrayList<String> breweryNames;
    private ArrayList<String> beerTypes;
    private ArrayList<String> aBv;
    private OnBeerListener mOnBeerListener;

    public RecyclerViewAdapter(ArrayList<String> beerNames, ArrayList<String> breweryNames,
                               ArrayList<String> beerTypes, ArrayList<String> aBv,
                               OnBeerListener mOnBeerListener) {

        this.beerNames = beerNames;
        this.breweryNames = breweryNames;
        this.beerTypes = beerTypes;
        this.aBv = aBv;
        this.mOnBeerListener = mOnBeerListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_beer, parent,
                false);
        return new ViewHolder(view, mOnBeerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.beer.setText(beerNames.get(position));
        holder.brewery.setText(breweryNames.get(position));
        holder.beerType.setText(beerTypes.get(position));
        holder.aBv.setText(aBv.get(position));
        holder.beerImg.setImageResource(R.mipmap.ic_launcher);

    }

    @Override
    public int getItemCount() {
        return beerNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView beer, brewery, beerType, aBv;
        public CircleImageView beerImg;
        public OnBeerListener onBeerListener;

        public ViewHolder(@NonNull View itemView, OnBeerListener onBeerListener) {
            super(itemView);
            beer = itemView.findViewById(R.id.beerName_textView);
            brewery = itemView.findViewById(R.id.breweryName_textView);
            beerType = itemView.findViewById(R.id.beerType_textView);
            aBv = itemView.findViewById(R.id.abv_textView);
            beerImg = itemView.findViewById(R.id.testImage);

            this.onBeerListener = onBeerListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "adapter position." + getAdapterPosition());
            onBeerListener.onBeerClick(getAdapterPosition());
        }
    }

    public interface OnBeerListener{
        void onBeerClick(int position);
    }

}
