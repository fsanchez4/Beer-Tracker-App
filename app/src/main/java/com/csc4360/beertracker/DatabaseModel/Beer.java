package com.csc4360.beertracker.DatabaseModel;
// (Table) Class for beer information with data population method

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "beers", indices = {@Index(value = "beer_name", unique = true),
        @Index(value = "beer_mfg"), @Index(value = "beer_type")})

public class Beer {

    // Columns in beer_table
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "beer_id")
    private int beer_id;

    @ColumnInfo(name = "beer_name")
    @NonNull
    private String name;

    @ColumnInfo(name = "beer_mfg")
    private String brewery;

    @ColumnInfo(name = "beer_type")
    private String type;

    @ColumnInfo(name = "alcohol_content")
    private String abv;

    // Empty constructor
    public Beer(){
        // Required empty constructor
    }

    // Constructor for populating table data
    @Ignore
    public Beer(@NonNull String name, String brewery, String type, String abv) {
        this.name = name;
        this.brewery = brewery;
        this.type = type;
        this.abv = abv;
    }


    // Getters and setters

    public int getBeer_id() {
        return beer_id;
    }

    public void setBeer_id( int beer_id) {
        this.beer_id = beer_id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }
}


