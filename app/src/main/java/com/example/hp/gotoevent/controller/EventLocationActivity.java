package com.example.hp.gotoevent.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hp.gotoevent.R;

import com.example.hp.gotoevent.bean.Event;
import com.example.hp.gotoevent.service.EventService;
import com.example.hp.gotoevent.util.MessageUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class EventLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static  final float DEFAULT_ZOOM = 10;
    private static final String TAG = "EventLocationActivity";
    private LatLng myLatLng = new LatLng(0,0);
    private Event event;

    private GoogleMap mMap;
    private PlaceAutocompleteFragment autocompleteFragment;
    private Button checkLocationBtn;

    private LatLng defaultLat;
    private Marker marker ;
    private Address address;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);

        setUIView();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                try {
                    marker.setPosition(place.getLatLng());
                    getLocation(myLatLng,place);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String placeName =  place.getName().toString();
                Log.i(TAG, "Place: " + place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("error occured === ");
                Log.i(TAG, " ===========  An error occurred: ===  " + status);
            }
        });

        checkLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();

            }
        });

    }

    private void geoLocation(double lat, double lg, float zoom){
        myLatLng = new LatLng(lat, lg);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLng,zoom);
        mMap.moveCamera(cameraUpdate);
    }

    private Address getLocation(LatLng myLatLng, Place place) throws IOException {
        Address address = isAddress(place);
        if(address !=  null){
            System.out.println("Adresse ================= " +address);
            String locality = address.getLocality();
            System.out.println("locality ================= " +locality);
            MessageUtil.shortToast(EventLocationActivity.this,locality);
            double lat = address.getLatitude();
            double lg = address.getLongitude();
            geoLocation(lat, lg , DEFAULT_ZOOM);
            marker.setPosition(myLatLng);
            System.out.println("LatLng selected ============ " +myLatLng.toString());
            return address;
        }
        return null;
    }



    public Address isAddress(Place place) throws IOException {
        String location = place.getName().toString();
        System.out.println("location ================ " +location);
        Geocoder gc = new Geocoder(EventLocationActivity.this);
        List<Address> addressList = gc.getFromLocationName(location, 1);
        if(addressList != null && addressList.size() > 0){
             Address address = addressList.get(0);
            System.out.println("address line ========================= "+ address.getAddressLine(0).toString());
             return address;
        }else
        {
            System.out.println("Walooooooooooooooooo ********************************");
            return null;
        }

    }

    private void setUIView (){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        checkLocationBtn = (Button) findViewById(R.id.getAdressBtn);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        defaultLat = new LatLng(0, 0);
        marker = mMap.addMarker(new MarkerOptions().position(defaultLat).title(""));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(!defaultLat.equals(latLng)){
                    marker.setPosition(latLng);
                    defaultLat = latLng;
                    myLatLng = latLng;
                    Geocoder gc = new Geocoder(EventLocationActivity.this,Locale.getDefault());
                    try {
                       List<Address> addressList = gc.getFromLocation(latLng.latitude,latLng.longitude, 1);
                        address = addressList.get(0);
                        marker.setTitle(address.getAddressLine(0));
                        Intent intent = new Intent();
                        double cordinate[] = {latLng.latitude,latLng.longitude};
                        intent.putExtra("LatLng" , cordinate);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getAddress(){
        Bundle bundle = getIntent().getExtras();
        String myEvent[] = bundle.getStringArray("event");
        Intent intent = new Intent(EventLocationActivity.this,NewEventActivity.class);
        intent.putExtra("myEvent" ,myEvent);
        intent.putExtra("getParam", true);
        if(myLatLng.latitude != 0 && myLatLng.longitude != 0){
            intent.putExtra("latitude" , myLatLng.latitude);
            intent.putExtra("longitude" , myLatLng.longitude);
            intent.putExtra("address",address.getAddressLine(0).toString());
            intent.putExtra("isNull", true);
        }
        startActivity(intent);
    }
}
