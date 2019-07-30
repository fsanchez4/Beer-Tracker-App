package com.csc4360.beertracker.DatabaseModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface BeerDao {

    @Query("SELECT * FROM beers")
    List<Beer> getAllBeers();

    @Query("SELECT beer_name FROM beers")
    List<String> getBeerNames();

    @Query("SELECT beer_mfg FROM beers")
    List<String> getBreweryNames();

    @Query("SELECT beer_type FROM beers")
    List<String> getBeerTypes();

    @Query("SELECT alcohol_content FROM beers")
    List<String> getBeerABVs();

    @Query("SELECT * FROM beers WHERE beer_name=:name")
    List<Beer> getBeerByName(String name);

    @Query("SELECT * FROM beers WHERE beer_id=:beerId")
    Beer getBeer(int beerId);

    @Query("SELECT * FROM beers WHERE beer_name=:name")
    Beer getBeerByStringName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Beer beer);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Beer... beers);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Beer beer);

    @Delete
    void delete(Beer beer);

    @Query("DELETE FROM beers")
    void deleteAll();

}
