package com.csc4360.beertracker.Controller;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csc4360.beertracker.DatabaseModel.AppDatabase;
import com.csc4360.beertracker.DatabaseModel.Beer;
import com.csc4360.beertracker.MainActivity;
import com.csc4360.beertracker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.test.InstrumentationRegistry.getContext;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> beerNames;
    private ArrayList<String> breweryNames;
    private ArrayList<String> beerTypes;
    private ArrayList<String> aBv;
    private ArrayList<Float> beerRatings;
    private ArrayList<String> beerImages;
    private OnBeerListener mOnBeerListener;


    public RecyclerViewAdapter(ArrayList<String> beerNames, ArrayList<String> breweryNames,
                               ArrayList<String> beerTypes, ArrayList<String> aBv,
                               ArrayList<Float> beerRatings, ArrayList<String> beerImages,
                               OnBeerListener mOnBeerListener) {

        this.beerNames = beerNames;
        this.breweryNames = breweryNames;
        this.beerTypes = beerTypes;
        this.aBv = aBv;
        this.beerRatings = beerRatings;
        this.beerImages = beerImages;
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
        holder.beerRating.setRating(beerRatings.get(position));

        if (isNumeric(beerImages.get(position))) {
            holder.beerImg.setImageResource(Integer.valueOf(beerImages.get(position)));
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(beerImages.get(position));
            holder.beerImg.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return beerNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView beer, brewery, beerType, aBv;
        public CircleImageView beerImg;
        public RatingBar beerRating;
        public OnBeerListener onBeerListener;

        public ViewHolder(@NonNull View itemView, OnBeerListener onBeerListener) {
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
            Log.d(TAG, "adapter position." + getAdapterPosition());

            onBeerListener.onBeerClick(getAdapterPosition());
        }
    }

    public interface OnBeerListener{
        void onBeerClick(int position);
    }

    public ArrayList<String> getBeerNames() {
        return beerNames;
    }

    public void setBeerNames(ArrayList<String> beerNames) {
        this.beerNames = beerNames;
    }

    public ArrayList<String> getBreweryNames() {
        return breweryNames;
    }

    public void setBreweryNames(ArrayList<String> breweryNames) {
        this.breweryNames = breweryNames;
    }

    public ArrayList<String> getBeerTypes() {
        return beerTypes;
    }

    public void setBeerTypes(ArrayList<String> beerTypes) {
        this.beerTypes = beerTypes;
    }

    public ArrayList<String> getaBv() {
        return aBv;
    }

    public void setaBv(ArrayList<String> aBv) {
        this.aBv = aBv;
    }

    public OnBeerListener getmOnBeerListener() {
        return mOnBeerListener;
    }

    public void setmOnBeerListener(OnBeerListener mOnBeerListener) {
        this.mOnBeerListener = mOnBeerListener;
    }

    public ArrayList<Float> getBeerRatings() {
        return beerRatings;
    }

    public void setBeerRatings(ArrayList<Float> beerRatings) {
        this.beerRatings = beerRatings;
    }

    public ArrayList<String> getBeerImages() {
        return beerImages;
    }

    public void setBeerImages(ArrayList<String> beerImages) {
        this.beerImages = beerImages;
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
