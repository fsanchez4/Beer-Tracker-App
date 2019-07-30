package com.csc4360.beertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.csc4360.beertracker.Controller.RecyclerViewAdapter;
import com.csc4360.beertracker.Controller.SearchAdapter;
import com.csc4360.beertracker.DatabaseModel.AppDatabase;
import com.csc4360.beertracker.DatabaseModel.Beer;
import com.csc4360.beertracker.DatabaseModel.BeerDao;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnBeerListener{

    private static final String TAG = "MainActivity";

    public static AppDatabase appDatabase;

    public RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;

    private List<Beer> data;
    private ArrayList<String> beerNames;
    private ArrayList<String> breweryNames;
    private ArrayList<String> beerTypes;
    private ArrayList<String> aBv;


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

        // RecyclerView
        Log.d(TAG, "initRecyclerView : running ... ");
        recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = setData();
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


        //initImageBitmaps();

    }

    // Method for adding pictures
//    private void initImageBitmaps() {
//        Log.d(TAG, "initImageBitmaps : preparing bitmaps...)");
//
//        initRecyclerView();
//    }

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

        for (int i = 0; i < data.size(); i++) {
            Beer beer = data.get(i);
            beerNames.add(beer.getName());
            breweryNames.add(beer.getBrewery());
            beerTypes.add(beer.getType());
            aBv.add(beer.getAbv());
        }

        // Creating new adapter
        adapter = new RecyclerViewAdapter(beerNames, breweryNames, beerTypes, aBv,
                this);

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
         <<<<<<< deleteAll-dialog-popUp

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

        for (int i = 0; i < data.size(); i++) {
            Beer beer = data.get(i);
            beerNames.add(beer.getName());
            breweryNames.add(beer.getBrewery());
            beerTypes.add(beer.getType());
            aBv.add(beer.getAbv());
        }
        adapter.setBeerNames(beerNames);
        adapter.setBreweryNames(breweryNames);
        adapter.setBeerTypes(beerTypes);
        adapter.setaBv(aBv);
        adapter.setmOnBeerListener(this);
        adapter.notifyDataSetChanged();
}

}
