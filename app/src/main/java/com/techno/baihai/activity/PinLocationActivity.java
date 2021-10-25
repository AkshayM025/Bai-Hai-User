package com.techno.baihai.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import com.techno.baihai.R;
import com.techno.baihai.databinding.ActivityPinLocationBinding;
import com.techno.baihai.utils.Tools;

import java.io.IOException;
import java.util.List;


/*

public class PinLocationActivity extends AppCompatActivity implements LocationListener {
    double lat = 0, lng = 0;
    private ActivityPinLocationBinding binding;
    private LocationManager locationManager;
    private Location location;
    private GoogleMap mMap;
    private Animation myAnim;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.select_location);
        initLocation();
        bindMap();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initLocation() {
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] arr = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
            requestPermissions(arr, 100);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        if (Tools.get().isLocationEnabled(this, locationManager) && location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            binding.tvAddress.setText(Tools.getCompleteAddressString(this, location.getLatitude(), location.getLongitude()));
        }
    }

    private void bindMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap = map;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (location != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                }
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        lat = mMap.getCameraPosition().target.latitude;
                        lng = mMap.getCameraPosition().target.longitude;
                        binding.tvAddress.setText(Tools.getCompleteAddressString(PinLocationActivity.this, lat, lng));
                        binding.imgMarker.startAnimation(myAnim);
                    }
                });
            }
        });
    }

    @Override
    public void onLocationChanged(Location loc) {
        location = loc;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pin_manu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (lat != 0) {
            Intent intent = new Intent();
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            setResult(111, intent);
            finish();
        } else {
            Toast.makeText(this, "Location Not selected", Toast.LENGTH_SHORT).show();
        }
    }
}
*/

public class PinLocationActivity extends AppCompatActivity implements LocationListener {
    private ActivityPinLocationBinding binding;
    private LocationManager locationManager;
    private Location location;
    private GoogleMap mMap;
    double lat=0,lng=0;
    String code,addressLoc="";

    private Animation myAnim;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_pin_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.select_location);
        initLocation();
        bindMap();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initLocation() {
        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] arr = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
            requestPermissions(arr, 100);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        if (Tools.get().isLocationEnabled(this,locationManager)&&location!=null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            binding.tvAddress1.setText(Tools.getCompleteAddressString(this, location.getLatitude(), location.getLongitude()));
        }
    }

    private void bindMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap=map;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (location!=null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                }
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        lat =  mMap.getCameraPosition().target.latitude;
                        lng =  mMap.getCameraPosition().target.longitude;
                        binding.tvAddress1.setText(Tools.getCompleteAddressString(PinLocationActivity.this,lat,lng));
                        binding.imgMarker.startAnimation(myAnim);
                    }
                });
            }
        });
    }


    public void searchLocation(View view) {
        EditText locationSearch = findViewById(R.id.tv_address);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    lat = address.getLatitude();
                    lng = address.getLongitude();
                    addressLoc = location;
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                Toast.makeText(getApplicationContext(), address.getLatitude() + " " + address.getLongitude(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Log.e("searchLocation:",e.getMessage());
            }
        }
    }



    @Override
    public void onLocationChanged(Location loc) {
        location=loc;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pin_manu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (lat!=0) {
            Intent intent = new Intent();
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            setResult(111, intent);
            finish();
        }else {
            Toast.makeText(this, "Location Not selected", Toast.LENGTH_SHORT).show();
        }
    }
}
