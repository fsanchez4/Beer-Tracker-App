package com.csc4360.beertracker.DatabaseModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;


@Database(entities = {Beer.class, Brewery.class, BeerTypes.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final String DB_NAME = "atl_beer.db";

    // Get INSTANCE method
    public static AppDatabase getDatabase(final Context context) {

        System.out.println("APPDATABASE :  GETDATABASE() IS RUNNING...");
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("APPDATABASE", "populating with data...");
                                    new PopulateDbAsync(INSTANCE).execute(); }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public void clearDb() {
        if (INSTANCE != null) {
            new PopulateDbAsync(INSTANCE).execute();
        }
    }

    public abstract BeerDao beerDao();
    public abstract BreweryDao breweryDao();
    public abstract BeerTypesDao beerTypesDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final BeerDao beerDao;
        private final BreweryDao breweryDao;
        private final BeerTypesDao beerTypesDao;

        public PopulateDbAsync(AppDatabase instance) {
            beerDao = instance.beerDao();
            breweryDao = instance.breweryDao();
            beerTypesDao = instance.beerTypesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            beerDao.deleteAll();
            breweryDao.deleteAll();
            beerTypesDao.deleteAll();

            Beer[] preBeerData = populateBeerData();
            System.out.println(Arrays.toString(preBeerData));

            Brewery[] preBreweryData = populateBreweryData();
            System.out.println(Arrays.toString(preBreweryData));

            BeerTypes[] preBeerTypeData = populateBeerTypesData();
            System.out.println(Arrays.toString(preBeerTypeData));

            for (Beer beer : preBeerData) {
                System.out.println("APPDATABASE :  POPULATING BEER DATA ...");

                beerDao.insert(beer);
            }

            for (Brewery brewery : preBreweryData) {
                System.out.println("APPDATABASE :  POPULATING BREWERY DATA ...");
                breweryDao.insert(brewery);
            }

            for (BeerTypes beerType : preBeerTypeData) {
                System.out.println("APPDATABASE : POPULATING TYPE DATA ...");
                beerTypesDao.insert(beerType);
            }
            return null;
        }
    }

    private static Beer[] populateBeerData() {

        return new Beer[]{
                new Beer("Rapturous", "Three Taverns", "Sour Ale",
                        "5%"),
                new Beer("Lord Gray", "Three Taverns", "Sour Ale",
                        "5%"),
                new Beer("A Night On Ponce", "Three Taverns", "IPA",
                        "7.5%"),
                new Beer("Transmigration of Souls", "Orpheus", "IPA",
                        "8%"),
                new Beer("The Ferryman", "Orpheus", "Stout", "11.8%"),

                new Beer("Classic City Lager", "Creature Comforts",
                        "Pale Lager & Pilsner", "4.2%"),
                new Beer("Everything Is...", "Creature Comforts",
                        "Pale Lager & Pilsner", "5.2%"),
                new Beer("Tropicalia", "Creature Comforts", "IPA",
                        "6.6%"),
                new Beer("Cosmik Debris", "Creature Comforts", "IPA",
                        "8%")
        };
    }

    private static Brewery[] populateBreweryData() {

        BreweryAddress threeTavernsAddress = new BreweryAddress("121", "New St",
                "Decatur", "GA", "30030");

        BreweryAddress orpheusAddress = new BreweryAddress("1440", "Dutch Vally Pl NE",
                "Atlanta", "GA", "30324");

        BreweryAddress creatureComfortsAddress = new BreweryAddress("271", "W Hancock Ave",
                "Athens", "GA", "30601");

        BreweryAddress abideAddress = new BreweryAddress("130", "Werz Industrial Blvd",
                "Newnan", "GA", "30263");

        BreweryAddress eventideAddress = new BreweryAddress("1015", "Grant St SE",
                "Atlanta", "GA", "30315");

        BreweryAddress maconAddress = new BreweryAddress("345", "Oglethorpe St",
                "Macon", "GA", "31201");

        BreweryAddress sweetwaterAddress = new BreweryAddress("195", "Ottley Dr NE",
                "Atlanta", "GA", "30324");

        BreweryAddress atlantaAddress = new BreweryAddress("2207", "Defoor Hills Rd NW",
                "Atlanta", "GA", "30318");

        return new Brewery[] {
                new Brewery("Three Taverns", "404-600-3355",
                        threeTavernsAddress.returnAddress()),

                new Brewery("Orpheus", "404-347-1777",
                        orpheusAddress.returnAddress()),

                new Brewery("Creature Comforts","706-410-1043",
                        creatureComfortsAddress.returnAddress()),

                new Brewery("Abide", "678-972-6747",
                        abideAddress.returnAddress()),

                new Brewery("Eventide", "404-907-4543",
                        eventideAddress.returnAddress()),

                new Brewery("Macon Beer Co.", "478-216-7117",
                        maconAddress.returnAddress()),

                new Brewery("Sweetwater", "404-691-2537",
                        sweetwaterAddress.returnAddress()),

                new Brewery("Atlanta Brewing Co.", "404-355-5558",
                        atlantaAddress.returnAddress())

        };
    }

    private static BeerTypes[] populateBeerTypesData() {
        return new BeerTypes[] {

                new BeerTypes("Pale Lager & Pilsner", "Golden-colored" +
                        " beers that are lighter in flavor and lower in alcohol content."),
                new BeerTypes("Dark Lager", "Malty and smooth, with" +
                        " toasted caramel notes. These beers tend to have mid-range alcohol content" +
                        " and lower bitterness profiles."),
                new BeerTypes("German Bock", "Heavy on malty flavor," +
                        " making them sweet and nutty. Bocks have lower alcohol levels, while" +
                        "doppelbocks, weizenbocks, and maibocks move up the alcohol scale."),
                new BeerTypes("Brown Ale", "Features malty overtones" +
                        " and tend to have toasty caramel flavors. They typically feature mid-range" +
                        " alcohol content and hop bitterness."),
                new BeerTypes("Pale Ale", "Generally hoppy but lower" +
                        " in alcohol content than IPAs. They are typically light, drinkable beers."),
                new BeerTypes("IPA", "Short for India Pale Ale, IPAs" +
                        " boast strong hop bitterness with piney and floral flavors. These beers" +
                        " have high alcohol content."),
                new BeerTypes("Porter", "Dark in color, and they" +
                        " feature flavors reminiscent of chocolate, coffee, and caramel. They" +
                        " tend to be more chocolately than brown ales, and less coffee-like" +
                        " than stouts."),
                new BeerTypes("Stout", "Dark beers that are similar" +
                        " to porters, but with stronger roasted flavors. This style also features" +
                        " mid to high alcohol levels."),
                new BeerTypes("Belgian Style", "Known for their spiced," +
                        " sweet, and fruity flavors and high alcohol content. Despite their high" +
                        " content, belgians are usually low in bitterness."),
                new BeerTypes("Wheat", "Uses wheat instead of malt." +
                        " They are generally lighter in color and alcohol content. Their tangy" +
                        " flavors go great with fruit, and brewers often add seasonal fruits to" +
                        " wheat beer."),
                new BeerTypes("Sour Ale", "Typically very low in" +
                        " alcohol, and feature tart, sour flavors that come from safe bacteria" +
                        " in the brew mash.")

        };
    }

}

