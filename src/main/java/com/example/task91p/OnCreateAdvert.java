package com.example.task91p;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OnCreateAdvert extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int MAP_ACTIVITY_REQUEST_CODE = 1002;

    private EditText locationText;
    private Button saveButton;
    private Button buttonCurrentLocation;
    private DatabaseClass database;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private RadioButton ButtonLost;
    private RadioButton ButtonFound;
    private EditText nameText;
    private EditText phoneText;
    private EditText descriptionText;
    private EditText dateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);

        database = new DatabaseClass(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ButtonLost = findViewById(R.id.lostButton);
        ButtonFound = findViewById(R.id.foundButton);
        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);
        descriptionText = findViewById(R.id.descriptionText);
        dateText = findViewById(R.id.datetText);
        locationText = findViewById(R.id.locationText);
        saveButton = findViewById(R.id.save);
        buttonCurrentLocation = findViewById(R.id.Current);
        //It will get the current location of the user
        buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        //It will open the Google Maps
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });
        //It will save the advert in the database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            locationText.setEnabled(true);
        }
    }
    //Open the Google Maps
    private void openMapActivity() {
        Intent intent = new Intent(this, LocationForMaps.class);
        startActivityForResult(intent, MAP_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("latitude") && data.hasExtra("longitude")) {
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                setLocationText(latitude, longitude);
            }
        }
    }

    //It will insert the advert into the database
    private void saveData() {
        String type = ButtonLost.isChecked() ? "Lost" : "Found";
        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        String description = descriptionText.getText().toString();
        String date = dateText.getText().toString();
        String location = locationText.getText().toString();
        Advert locationAdvert = new Advert(type, name, phone, description, date, location, 0);
        long rowId = database.insertData(locationAdvert);
        if (rowId != -1) {
            finish();
        } else {

        }
    }

    //It will get the current location
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            setLocationText(latitude, longitude);
                        }
                    }
                }
            };
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    //It will check the permission is granted or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationText.setEnabled(true);
            } else {
                locationText.setEnabled(false);
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    // It will convert latitude and longitude to address
    private void setLocationText(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                locationText.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
