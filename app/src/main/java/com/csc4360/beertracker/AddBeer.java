package com.csc4360.beertracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AddBeer extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        // Adding App Bar
        Toolbar appToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.addBeer_container, new AddBeerFragment())
                    .commit();
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.addBeer_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
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
                startActivity(new Intent(AddBeer.this, SearchActivity.class));
                return true;

            case R.id.add_beer_action:
                // User chose the "Add" action
                return true;

            case R.id.delete_beer_action:
                // User chose the "Delete" action
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBeer.this);

                builder.setTitle("Warning!");
                builder.setMessage("Are you sure you want to delete everything?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.appDatabase.beerDao().deleteAll();
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

//    public static boolean hasPermissions(final Activity activity, final String permission,
//                                         String rationaleMessageId, final int requestWriteCode) {
//        // See if permission is granted
//        if (ContextCompat.checkSelfPermission(activity, permission)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Explain why permission needed?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//
//                // Beg for permission
//                showPermissionRationaleDialog(activity, rationaleMessageId, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        // Request permission again
//                        ActivityCompat.requestPermissions(activity,
//                                new String[] { permission }, requestWriteCode);
//                    }
//                });
//            }
//            else {
//                // Request permission
//                ActivityCompat.requestPermissions(activity,
//                        new String[] { permission }, requestWriteCode);
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private static void showPermissionRationaleDialog(Activity activity, String message,
//                                                      DialogInterface.OnClickListener onClickListener) {
//        // Show dialog explaining why permission is needed
//        new AlertDialog.Builder(activity)
//                .setTitle("Permission Needed")
//                .setMessage(message)
//                .setPositiveButton("OK", onClickListener)
//                .create()
//                .show();
//    }
}
