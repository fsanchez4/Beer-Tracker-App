package com.csc4360.beertracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.csc4360.beertracker.DatabaseModel.Beer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class AddBeerFragment extends Fragment {

    private static final int REQUEST_EXTERNAL_WRITE_PERMISSIONS = 0;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "AddBeerFragment";

    private String mPhotoPath;
    String currentImagePath = null;

    private EditText beerTextInput, abvTextInput;
    private Spinner brewerySpinner, beerType_spinner;
    private RatingBar rate;
    private ImageView imageView;
    private Button addImageButton;

    private String picName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_beer, container, false);

        beerTextInput = view.findViewById(R.id.textInput_layout1);
        brewerySpinner = view.findViewById(R.id.breweryOptions);
        abvTextInput = view.findViewById(R.id.textInput_layout3);
        beerType_spinner = view.findViewById(R.id.beerType_spinner);
        rate = view.findViewById(R.id.userRating);

        imageView = view.findViewById(R.id.imageView);
        addImageButton = view.findViewById(R.id.add_image_bn);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoClick(view);
            }
        });
//
//        Bitmap bitmap = BitmapFactory.decodeFile("image_path");
//        imageView.setImageBitmap(bitmap);

        Button submitButton = view.findViewById(R.id.save_bn);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name_db = beerTextInput.getText().toString();
                String brewery_db = brewerySpinner.getSelectedItem().toString();
                String abv_db = abvTextInput.getText().toString();
                String type_db = beerType_spinner.getSelectedItem().toString();
                float rating_db = rate.getRating();
                String image_db = mPhotoPath;
                Log.d(TAG, "mPhotoPath : " + mPhotoPath);

                // TEST: checking if rating value is working
                Log.d("*** USER RATING: ", "Rating for beer: " + rating_db);

                // New beer object
                Beer beer = new Beer(name_db, brewery_db, type_db, abv_db, rating_db, image_db);

                // DB add
                MainActivity.appDatabase.beerDao().insert(beer);
                int position = MainActivity.appDatabase.beerDao().getBeerNames().size();
                MainActivity.adapter.notifyItemInserted(position);

                Toast.makeText(getActivity(), "Beer added successfully..", Toast.LENGTH_LONG)
                        .show();

                // Set view
                beerTextInput.setText("");
                abvTextInput.setText("");
                brewerySpinner.setSelection(0);
                beerType_spinner.setSelection(0);
                rate.setRating(0);
                imageView.setImageAlpha(0);

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

//    public void captureImage(View view) {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            File imageFile = null;
//
//            try {
//                imageFile = getImageFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (imageFile != null) {
//                Uri imageUri = FileProvider.getUriForFile(getActivity(), "com.csc4360.android.fileprovider",
//                        imageFile);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(cameraIntent, REQUEST_READ_CODE);
//            }
//        }
//    }
//
//    public void displayImage(View view) {
//
//    }
//
//    private File getImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
//        String imageName = "jpg_" + timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
//        currentImagePath = imageFile.getAbsolutePath();
//        return imageFile;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            displayPhoto();
            addPhotoToGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_WRITE_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoClick(null);
                }
                return;
            }
        }
    }

    private boolean hasExternalWritePermission() {
        // Get permission to write to external storage
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(getActivity(),
                permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { permission }, REQUEST_EXTERNAL_WRITE_PERMISSIONS);
            return false;
        }

        return true;
    }

    public void takePhotoClick(View view) {
        if (!hasExternalWritePermission()) return;

        // Create implicit intent
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCaptureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                mPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }

            // If the File was successfully created, start camera app
            if (photoFile != null) {

                // Create content URI to grant camera app write permission to photoFile
                Uri photoUri = FileProvider.getUriForFile(getActivity(),
                        "com.csc4360.beertracker.fileprovider",
                        photoFile);

                // Add content URI to intent
                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                // Start camera app
                startActivityForResult(photoCaptureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
//        // Create a unique filename
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFilename = "photo_" + timeStamp + ".jpg";
//
//        // Create the file in the Pictures directory on external storage
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = new File(storageDir, imageFilename);
//        return image;
        picName = beerTextInput.getText().toString().toLowerCase().replace(" ", "");


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageName = picName + "_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void displayPhoto() {
        // Get ImageView dimensions
        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        // Get bitmap dimensions
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoPath, bitmapOptions);
        int photoWidth = bitmapOptions.outWidth;
        int photoHeight = bitmapOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);

        // Decode the image file into a smaller bitmap that fills the ImageView
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;
        bitmapOptions.inPurgeable = true;

        Log.d(TAG, "displayPhoto: PHOTOPATH ... " + mPhotoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, bitmapOptions);

        // Display smaller bitmap
        imageView.setImageBitmap(bitmap);
    }

    private void addPhotoToGallery() {
        // Send broadcast to Media Scanner about new image file
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mPhotoPath);
        Uri fileUri = Uri.fromFile(file);
        mediaScanIntent.setData(fileUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

}
