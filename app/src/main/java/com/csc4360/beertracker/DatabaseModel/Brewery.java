package com.csc4360.beertracker.DatabaseModel;

// (Table) Class for brewery information (table)

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "breweries", indices = {@Index(value = "brewery_id", unique = true),
        @Index(value = "brewery_name", unique = true)})
public class Brewery {

    // Columns of brewery-table
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "brewery_id")
    @NonNull
    private int brewery_id;

    @ColumnInfo(name = "brewery_name")
    private String breweryName;

    @ColumnInfo(name = "brewery_number")
    private String breweryPhoneNumber;

    @ColumnInfo(name = "brewery_address")
    private String breweryAddress;


    public Brewery() {
        // Required empty constructor
    }

    // Constructor for pre-populating database
    @Ignore
    public Brewery(String breweryName, String breweryPhoneNumber, String breweryAddress) {
        this.breweryName = breweryName;
        this.breweryPhoneNumber = breweryPhoneNumber;
        this.breweryAddress = breweryAddress;
    }


    // Getters and setters
    public int getBrewery_id() {
        return brewery_id;
    }

    public void setBrewery_id(int brewery_id) {
        this.brewery_id = brewery_id;
    }

    @NonNull
    public String getBreweryName() {
        return breweryName;
    }

    public void setBreweryName(@NonNull String breweryName) {
        this.breweryName = breweryName;
    }

    public String getBreweryPhoneNumber() {
        return breweryPhoneNumber;
    }

    public void setBreweryPhoneNumber(String breweryPhoneNumber) {
        this.breweryPhoneNumber = breweryPhoneNumber;
    }

    public String getBreweryAddress() {
        return breweryAddress;
    }

    public void setBreweryAddress(String breweryAddress) {
        this.breweryAddress = breweryAddress;
    }

}
