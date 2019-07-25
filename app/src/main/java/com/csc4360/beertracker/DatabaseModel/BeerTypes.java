package com.csc4360.beertracker.DatabaseModel;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "beer_types")
public class BeerTypes {

    // Columns in user_preferences table
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "type_id")
    private int type_id;

    @ColumnInfo(name = "beer_type")
    private String beerType;

    @ColumnInfo(name = "beer_descriptions")
    private String beerTypeDescription;


    // Empty constructor
    public BeerTypes(){}

    // Constructor for pre-populating data
    public BeerTypes(String beerType, String beerTypeDescription) {
        this.beerType = beerType;
        this.beerTypeDescription = beerTypeDescription;
    }


    // Getters and setters
    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getBeerType() {
        return beerType;
    }

    public void setBeerType(String beerType) {
        this.beerType = beerType;
    }

    public String getBeerTypeDescription() {
        return beerTypeDescription;
    }

    public void setBeerTypeDescription(String beerTypeDescription) {
        this.beerTypeDescription = beerTypeDescription;
    }

}