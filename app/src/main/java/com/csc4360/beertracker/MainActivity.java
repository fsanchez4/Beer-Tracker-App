package com.csc4360.beertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.csc4360.beertracker.Controller.RecyclerViewAdapter;
import com.csc4360.beertracker.DatabaseModel.AppDatabase;
import com.csc4360.beertracker.DatabaseModel.Beer;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnBeerListener{

    private static final String TAG = "MainActivity";

    public static AppDatabase appDatabase;

    public RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;

    public static List<Beer> data;
    public static List<MarkerOptions> mBreweryMarkers = null;

    private ArrayList<String> beerNames;
    private ArrayList<String> breweryNames;
    private ArrayList<String> beerTypes;
    private ArrayList<String> aBv;
    private ArrayList<Float> beerRatings;
    private ArrayList<String> beerImages;

  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate : started...");

        // Adding App Bar
        Toolbar appToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(appToolbar);

        // AppDatabase
        appDatabase = AppDatabase.getDatabase(this);
        data = appDatabase.beerDao().getAllBeers();

        // Data check
//         for (int i = 0; i < data.size(); i++) {
//             System.out.println(data.get(i).getBeer_id() + ", " + beerNames.get(i) + ", " + breweryNames.get(i)
//                     + ", " + beerTypes.get(i) + ", "  + aBv.get(i));
//             System.out.println("\n");
//         }

        Thread getMapData = new Thread(new Runnable(){
            @Override
            public void run() {
                    //Pull in list of address from DB
                String[] nameListFromDB = appDatabase.breweryDao().getAllBreweryNames();
                String[] addressListFromDB = appDatabase.breweryDao().getAllAddresses();


                //Use geocoder to return list of addressFromGeocoder which contain coordinate information
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);

                    //List of type address that is returned from geocoder
                    List<Address> addressFromGeocoder;

                    //Temporary variables
                    LatLng tempCoordinates;
                    List<LatLng> coordinatesList = new ArrayList<>();

                    //For each address in the DB, get the full geocoder returned address
                    for (int i = 0; i < addressListFromDB.length; i++) {
                        try {
                            addressFromGeocoder = geocoder.getFromLocationName(addressListFromDB[i], 1);
                            tempCoordinates = new LatLng(addressFromGeocoder.get(0).getLatitude(), addressFromGeocoder.get(0).getLongitude());
                            coordinatesList.add(tempCoordinates);
                            System.out.println("--------------INIT COORDINATE PULL---------------------------" + tempCoordinates);

                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                    //Create list of markers from brewery coordinates
                    mBreweryMarkers = new ArrayList<>(coordinatesList.size());
                    for (LatLng l : coordinatesList) {
                        mBreweryMarkers.add(new MarkerOptions().title("Fix This Later!").position(l));
                    }
                    for(int i=0;i<mBreweryMarkers.size();i++){
                        mBreweryMarkers.get(i).title(nameListFromDB[i]);
                    }
                    for (MarkerOptions m : mBreweryMarkers) {
                        System.out.println("-------------MARKER CHECK COORDINATES------------------" + m.getPosition().toString());
                    }
                }
        });
        getMapData.start();

        // RecyclerView
        Log.d(TAG, "initRecyclerView : running ... ");
        recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = setData();
        // new ItemTouchHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        // RecyclerView divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        // OnSwipe Delete
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                appDatabase.beerDao().delete(data.get(position));
                data.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, data.size());
                updateAdapter();
                // recyclerView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Beer deleted...", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        helper.attachToRecyclerView(recyclerView);

    }

    public RecyclerViewAdapter setData() {

        Log.d(TAG, "setData: DATA ... " + data);

        for (int i = 0; i < 10; i++) {
            if (data.size() < 10) {
                data = appDatabase.beerDao().getAllBeers();
                if (data.size() == 9) {
                    Log.d(TAG, "setData: PRINT i ..." + i);
                    break;
                }
            }
        }

        // Data structures for organizing data on main recyclerView prepopulate
        beerNames = new ArrayList<>(data.size());
        breweryNames = new ArrayList<>(data.size());
        beerTypes = new ArrayList<>(data.size());
        aBv = new ArrayList<>(data.size());
        beerRatings = new ArrayList<>(data.size());
        beerImages = new ArrayList<>(data.size());

        for (int i = 0; i < data.size(); i++) {
            Beer beer = data.get(i);
            beerNames.add(beer.getName());
            breweryNames.add(beer.getBrewery());
            beerTypes.add(beer.getType());
            aBv.add(beer.getAbv());
            beerRatings.add(beer.getRating());
            beerImages.add(beer.getBeerImage());
        }

        // Creating new adapter
        adapter = new RecyclerViewAdapter(beerNames, breweryNames, beerTypes, aBv,
                beerRatings, beerImages, this);

        Log.d(TAG, "setData: ADAPTER = " + adapter);
        return adapter;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_app_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_map_action:
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                return true;

            case R.id.search_beer_action:
                Toast.makeText(this, "Search option selected...", Toast.LENGTH_SHORT)
                        .show();
                // User chose the "Search" item, show search bar
                startActivity(new Intent (MainActivity.this, SearchActivity.class));
                return true;

            case R.id.add_beer_action:
                // User chose the "Add" action
                Log.d(TAG, "onClick : Add Beer pressed!");
                startActivity(new Intent(MainActivity.this, AddBeer.class));
                return true;

            case R.id.delete_beer_action:
                // User chose the "Delete" action

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Warning!");
                builder.setMessage("Are you sure you want to delete everything?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        appDatabase.beerDao().deleteAll();
                        data.clear();
                        updateAdapter();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
  
    @Override
    public void onBeerClick(int position) {

        Log.d(TAG, "onBeerClicked." + position);
        Intent intent = new Intent(this, DetailsActivity.class);
        // intent.putExtra(DetailsActivity.EXTRA_BEER_ID, position);
        // appDatabase = AppDatabase.getDatabase(this);
        // setData();
        updateAdapter();
        intent.putExtra(DetailsActivity.EXTRA_BEER_NAME, data.get(position).getName());
        startActivity(intent);
    }

    public void updateAdapter() {
        // Beer list
        data = appDatabase.beerDao().getAllBeers();

        // Data structures for organizing data on main recyclerView prepopulate
        beerNames = new ArrayList<>(data.size());
        breweryNames = new ArrayList<>(data.size());
        beerTypes = new ArrayList<>(data.size());
        aBv = new ArrayList<>(data.size());
        beerRatings = new ArrayList<>(data.size());
        beerImages = new ArrayList<>(data.size());

        for (int i = 0; i < data.size(); i++) {
            Beer beer = data.get(i);
            beerNames.add(beer.getName());
            breweryNames.add(beer.getBrewery());
            beerTypes.add(beer.getType());
            aBv.add(beer.getAbv());
            beerRatings.add(beer.getRating());
            beerImages.add(beer.getBeerImage());

        }
        adapter.setBeerNames(beerNames);
        adapter.setBreweryNames(breweryNames);
        adapter.setBeerTypes(beerTypes);
        adapter.setaBv(aBv);
        adapter.setBeerRatings(beerRatings);
        adapter.setBeerImages(beerImages);
        adapter.setmOnBeerListener(this);
        adapter.notifyDataSetChanged();
    }

}
