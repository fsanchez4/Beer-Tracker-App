package com.csc4360.beertracker;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.csc4360.beertracker.DatabaseModel.Beer;


public class AddBeerFragment extends Fragment {

    private EditText beerTextInput;
    // private EditText breweryTextInput;
    private EditText abvTextInput;
    private Spinner brewerySpinner;
    private Spinner beerType_spinner;
    private Button submitButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_beer, container, false);

        beerTextInput = view.findViewById(R.id.textInput_layout1);
        //breweryTextInput = view.findViewById(R.id.textInput_layout2);
        brewerySpinner = view.findViewById(R.id.breweryOptions);
        abvTextInput = view.findViewById(R.id.textInput_layout3);
        beerType_spinner = view.findViewById(R.id.beerType_spinner);
        submitButton = view.findViewById(R.id.save_bn);

        submitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String name_db = beerTextInput.getText().toString();
                //String brewery_db = breweryTextInput.getText().toString();
                String brewery_db = brewerySpinner.getSelectedItem().toString();
                String abv_db = abvTextInput.getText().toString();
                String type_db = beerType_spinner.getSelectedItem().toString();

                // New beer object
                Beer beer = new Beer(name_db,brewery_db,type_db,abv_db);

                // DB add
                MainActivity.appDatabase.beerDao().insert(beer);
                MainActivity.adapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Beer added successfully", Toast.LENGTH_LONG)
                        .show();

                beerTextInput.setText("");
                //breweryTextInput.setText("");
                abvTextInput.setText("");
                brewerySpinner.setSelection(0);
                beerType_spinner.setSelection(0);

            }
        });

        // Obtain brewery spinner selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.breweries_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        brewerySpinner.setAdapter(adapter);
        brewerySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });


        // Obtain beer type spinner selection
        ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.beerTypes_array, android.R.layout.simple_spinner_item);

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        beerType_spinner.setAdapter(mAdapter);
        beerType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

        return view;
    }


}
