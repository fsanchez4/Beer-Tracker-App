package com.csc4360.beertracker;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.csc4360.beertracker.Controller.RecyclerViewAdapter;
import com.csc4360.beertracker.DatabaseModel.AppDatabase;
import com.csc4360.beertracker.DatabaseModel.Beer;
import com.csc4360.beertracker.DatabaseModel.BeerTypes;
import com.csc4360.beertracker.DatabaseModel.Brewery;
import com.csc4360.beertracker.DatabaseModel.BreweryAddress;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment.beerId";

    private Beer mBeer;
    private Brewery mBrewery;
    private BeerTypes mBeerType;

//    public static DetailsFragment newInstance(int beerId) {
//        // Required empty public constructor
//
//        DetailsFragment fragment = new DetailsFragment();
//        Bundle args = new Bundle();
//        args.putInt("beer_id", beerId);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static DetailsFragment newInstance(String beerName) {

        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("beer_name", beerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int beerId = 1;
//
//        if (getArguments() != null) {
//            beerId = getArguments().getInt("beer_id");
//        }

        String beerName = "";

        if (getArguments() != null) {
            beerName = getArguments().getString("beer_name");
        }

        Log.d(TAG, "onCreate : " + beerName);

        try {
            mBeer = MainActivity.appDatabase.beerDao().getBeerByStringName(beerName);
            System.out.println(mBeer.getName());
            mBrewery = MainActivity.appDatabase.breweryDao().getBrewery(mBeer.getBrewery());
            System.out.println(mBrewery.getBreweryName());
            mBeerType = MainActivity.appDatabase.beerTypesDao().getBeerType(mBeer.getType());
            System.out.println(mBeerType.getBeerType());
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Beer Info

        CircleImageView beerImage = view.findViewById(R.id.testImage);

        if (isNumeric(mBeer.getBeerImage())) {
            beerImage.setImageResource(Integer.valueOf(mBeer.getBeerImage()));
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(mBeer.getBeerImage());
            beerImage.setImageBitmap(bitmap);
        }

        TextView beerNameTextView = view.findViewById(R.id.beerName_textView);
        beerNameTextView.setText(mBeer.getName());

        TextView breweryNameTextView = view.findViewById(R.id.breweryName_textView);
        breweryNameTextView.setText(mBeer.getBrewery());

        TextView beerTypeTextView = view.findViewById(R.id.beerType_textView);
        beerTypeTextView.setText(mBeer.getType());

        TextView beerAbvTextView = view.findViewById(R.id.abv_textView);
        beerAbvTextView.setText(mBeer.getAbv());

        RatingBar beerRatingBar = view.findViewById(R.id.ratingBar);
        beerRatingBar.setRating(mBeer.getRating());

        // Type Info

        TextView typeTextView = view.findViewById(R.id.typeName);
        typeTextView.setText(mBeerType.getBeerType());

        TextView typeDescriptionTextView = view.findViewById(R.id.typeDescription);
        typeDescriptionTextView.setText(mBeerType.getBeerTypeDescription());

        // Brewery Info

        TextView breweryName = view.findViewById(R.id.breweryName);
        breweryName.setText(mBrewery.getBreweryName());

        TextView breweryAddress = view.findViewById(R.id.breweryAddress);
        breweryAddress.setText(mBrewery.getBreweryAddress());

        TextView breweryPhoneNumber = view.findViewById(R.id.breweryPhoneNumber);
        breweryPhoneNumber.setText(mBrewery.getBreweryPhoneNumber());

        return view;
    }

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
