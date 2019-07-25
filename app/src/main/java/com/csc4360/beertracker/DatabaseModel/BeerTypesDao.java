package com.csc4360.beertracker.DatabaseModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BeerTypesDao {

//    @Query("SELECT * FROM beer_types WHERE type_id")
//    int getBeerTypeId();

//    @Query("SELECT beer_type FROM beer_types WHERE type_id LIMIT 1")
//    BeerTypes findBeerTypeById(int type_id);
//
//    @Query("SELECT * FROM beer_types ORDER BY beer_type ASC")
//    List<BeerTypes> getAllTypes();

//    @Query("SELECT beer_type FROM beer_types")
//    String[] getBeerTypes();

    @Query("SELECT * FROM beer_types WHERE beer_type=:beerType")
    BeerTypes getBeerType(String beerType);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(BeerTypes beerType);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BeerTypes beerType);

    @Delete
    void delete(BeerTypes beerType);

    @Query("DELETE FROM beer_types")
    void deleteAll();

}