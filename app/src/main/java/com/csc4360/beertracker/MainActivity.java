package com.csc4360.beertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.csc4360.beertracker.Controller.RecyclerViewAdapter;
import com.csc4360.beertracker.Controller.SearchAdapter;
import com.csc4360.beertracker.DatabaseModel.AppDatabase;
import com.csc4360.beertracker.DatabaseModel.Beer;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnBeerListener{

    private static final String TAG = "MainActivity";

    public static AppDatabase appDatabase;

    public RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    private List<Beer> data;

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
