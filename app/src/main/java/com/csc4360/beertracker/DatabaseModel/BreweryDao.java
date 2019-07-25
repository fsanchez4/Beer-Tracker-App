package com.csc4360.beertracker.DatabaseModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BreweryDao {

//    @Query("SELECT * FROM breweries WHERE id=:id LIMIT 1")
//    Brewery findBreweryById(int id);

//    @Query("SELECT * FROM breweries WHERE brewery_id")
//    int getBeerId();
//
//    @Query("SELECT * FROM breweries ORDER BY brewery_name ASC")
//    List<Brewery> getAllBreweries();
//
//    @Query("SELECT brewery_name FROM breweries")
//    String[] getBreweryNames();

    @Query("SELECT * FROM breweries WHERE brewery_name=:breweryName")
    Brewery getBrewery(String breweryName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Brewery brewery);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBreweries(Brewery... breweries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Brewery brewery);

    @Delete
    void delete(Beer beer);

    @Query("DELETE FROM beers")
    void deleteAll();
}
