package com.csc4360.beertracker;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csc4360.beertracker.Controller.SearchAdapter;
import com.csc4360.beertracker.DatabaseModel.Beer;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchAdapter.OnBeerListener {

    // For Material Search Bar
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private MaterialSearchBar materialSearchBar;
    private List<String> suggestList = new ArrayList<>();

    // Data
    private Beer beer;

    // private static AppDatabase appDatabase;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        // Material search bar
        recyclerView = view.findViewById(R.id.search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);

        // RecyclerView divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        materialSearchBar = view.findViewById(R.id.s_bar);
        materialSearchBar.setHint("Search");
        materialSearchBar.setCardViewElevation(10);
        loadSuggestList();

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }

                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) {
                    recyclerView.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        // Init adapter default set all result
        searchAdapter = new SearchAdapter(this.getActivity(), MainActivity.appDatabase.beerDao().getAllBeers(),
                this);
        recyclerView.setAdapter(searchAdapter);

        return view;
    }

    // Material search bar start method
    private void startSearch(String text) {

        searchAdapter = new SearchAdapter(this.getActivity(),
                MainActivity.appDatabase.beerDao().getBeerByName(text), this);
        recyclerView.setAdapter(searchAdapter);
    }

    // Method for suggested queries in material search bar
    private void loadSuggestList() {
        suggestList = MainActivity.appDatabase.beerDao().getBeerNames();
        materialSearchBar.setLastSuggestions(suggestList);

    }

    @Override
    public void onBeerClick(String searchName) {
        beer = MainActivity.appDatabase.beerDao().getBeerByStringName(searchName);
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_BEER_NAME, beer.getName());
        startActivity(intent);
    }
}

