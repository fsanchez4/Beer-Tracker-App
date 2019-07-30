package com.csc4360.beertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        // Data structures for organizing data on main recyclerView prepopulate
        ArrayList<String> beerNames = new ArrayList<>(data.size());
        ArrayList<String> breweryNames = new ArrayList<>(data.size());
        ArrayList<String> beerTypes = new ArrayList<>(data.size());
        ArrayList<String> aBv = new ArrayList<>(data.size());

        for (int i = 0; i < data.size(); i++) {
            Log.d(TAG, "ARRAYLISTS BEING POPULATED ...");

            Beer beer = data.get(i);

            beerNames.add(beer.getName());
            breweryNames.add(beer.getBrewery());
            beerTypes.add(beer.getType());
            aBv.add(beer.getAbv());
        }

        // Data check
        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i).getBeer_id() + ", " + beerNames.get(i) + ", " + breweryNames.get(i)
                    + ", " + beerTypes.get(i) + ", "  + aBv.get(i));
            System.out.println("\n");
        }

        Thread getMapData = new Thread(new Runnable(){
            @Override
            public void run() {
                    //Pull in list of address from DB
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
                    for (MarkerOptions m : mBreweryMarkers) {
                        System.out.println("-------------MARKER CHECK COORDINATES------------------" + m.getPosition().toString());
                    }
                }
        });
        getMapData.start();

        // RecyclerView
        Log.d(TAG, "initRecyclerView : init recyclerview...");
        recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(beerNames, breweryNames, beerTypes,
                aBv, this);
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
                removeItem(position);
                Toast.makeText(MainActivity.this, "Beer deleted...", Toast.LENGTH_SHORT)
                        .show();

            }
        });

        helper.attachToRecyclerView(recyclerView);


        //initImageBitmaps();

    }

    // Method for adding pictures
//    private void initImageBitmaps() {
//        Log.d(TAG, "initImageBitmaps : preparing bitmaps...)");
//
//        initRecyclerView();
//    }

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
                appDatabase.beerDao().deleteAll();
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
        intent.putExtra(DetailsActivity.EXTRA_BEER_ID, position);
        startActivity(intent);
    }

    private void removeItem(int id) {
        appDatabase.beerDao().delete(data.get(id));
        adapter.notifyDataSetChanged();
    }

}
