package com.toxic.salonapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "done" ;
    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private DatabaseReference refDatabase;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refDatabase = FirebaseDatabase.getInstance().getReference("Salon_Post");
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


        LatLng  tilok = new LatLng(-6.8850987, 106.7766293);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tilok));
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(15.0f);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        addMarkersToMap(googleMap);


    }

    private void addMarkersToMap(GoogleMap googleMap) {
        mChildEventListener = refDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SalonPost marker = dataSnapshot.getValue(SalonPost.class);
                String namasalon = marker.getNama_salon();
                double latitude = marker.getLatitude();
                double longitude = marker.getLongitude();
                LatLng location = new LatLng(latitude,longitude);
                mMap.addMarker(new MarkerOptions().position(location).title("Salon " + namasalon));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
