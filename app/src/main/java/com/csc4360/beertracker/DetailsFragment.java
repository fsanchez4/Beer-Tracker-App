package com.csc4360.beertracker;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csc4360.beertracker.DatabaseModel.AppDatabase;
import com.csc4360.beertracker.DatabaseModel.Beer;
import com.csc4360.beertracker.DatabaseModel.BeerTypes;
import com.csc4360.beertracker.DatabaseModel.Brewery;
import com.csc4360.beertracker.DatabaseModel.BreweryAddress;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private Beer mBeer;
    private Brewery mBrewery;
    private BeerTypes mBeerType;

    public static DetailsFragment newInstance(int beerId) {
        // Required empty public constructor

        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("beer_id", beerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int beerId = 1;

        if (getArguments() != null) {
            beerId = getArguments().getInt("beer_id");
        }

        try {
            mBeer = MainActivity.appDatabase.beerDao().getBeer(beerId);
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
        beerImage.setImageResource(R.mipmap.ic_launcher);

        TextView beerNameTextView = view.findViewById(R.id.beerName_textView);
        beerNameTextView.setText(mBeer.getName());

        TextView breweryNameTextView = view.findViewById(R.id.breweryName_textView);
        breweryNameTextView.setText(mBeer.getBrewery());

        TextView beerTypeTextView = view.findViewById(R.id.beerType_textView);
        beerTypeTextView.setText(mBeer.getType());

        TextView beerAbvTextView = view.findViewById(R.id.abv_textView);
        beerAbvTextView.setText(mBeer.getAbv());

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

}
