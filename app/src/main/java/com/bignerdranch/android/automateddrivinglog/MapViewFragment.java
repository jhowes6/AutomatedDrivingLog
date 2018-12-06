package com.bignerdranch.android.automateddrivinglog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.lang.Math.toIntExact;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Period;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


//************************************************************************************************
//MapViewFragment
//      *Class which contains a SupportMapFragment to display user location while driving

//************************************************************************************************
public class MapViewFragment extends SupportMapFragment {
    private static final String TAG = "MapViewFragment";
    private boolean stopped = false;

    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private Bitmap mMapImage;
    private Chronometer timer;

    private Location mCurrentLocation;
    private Location mNewLocation;

    public static MapViewFragment newInstance() { return new MapViewFragment(); }

    //*******************************************************************************************
    //onCreate(Bundle savedInstanceState)
    //      *instantiates mClient
    //      *instantiates mMap
    //
    //*******************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }
                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }
    //*******************************************************************************************
    //onStart()
    //      *connects mClient
    //
    //*******************************************************************************************
    @Override
    public void onStart(){
        super.onStart();

        getActivity().invalidateOptionsMenu();
        mClient.connect();

    }
    //*******************************************************************************************
    //onStop()
    //      *disconnects mClient
    //
    //*******************************************************************************************
    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }
    //*******************************************************************************************
    //onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    //      *inflates options menu
    //      *instantiates timer
    //
    //*******************************************************************************************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);

        MenuItem startItem = menu.findItem(R.id.action_locate);
        startItem.setEnabled(true);

        MenuItem stopItem = menu.findItem(R.id.action_stop);
        stopItem.setEnabled(true);

        timer = new Chronometer(getActivity());
    }
    //*******************************************************************************************
    //onOptionsItemSelected(MenuItem item)
    //      *if item = action_locate: call startDriving()
    //      *if item = action_stop: call stopDriving()
    //
    //*******************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_locate:
                startDriving();
                //item.setEnabled(false);
                return true;
            case R.id.action_stop:
                stopDriving();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //*******************************************************************************************
    //startDriving()
    //      *starts the timer
    //      *calls findImageDriving()
    //
    //*******************************************************************************************
    private void startDriving(){
        findImage();
        timer.start();
        long seconds = 0;
        do{
            long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();
            seconds = (elapsedMillis / 1000);

        }while(seconds < 10);
        findImageDriving();
    }
    //*******************************************************************************************
    //stopDriving()
    //      *stops the timer
    //      *calls findImage()
    //      *calculates duration of session and calls addToLog() to add the driving session to
    //       the log
    //      *launches a new LogActivity
    //
    //*******************************************************************************************
    private void stopDriving(){

        timer.stop();
        //findImage();
        long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();
        long seconds = (elapsedMillis / 1000);
        //int sec = toIntExact(seconds);
        Date d = new Date(seconds * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(d);
        Log.i(TAG, "Elapsed time: " + time + "\n");
        System.out.print("Elapsed time: " + time + "\n");
        addToLog(time);
        Intent i = LogActivity.newIntent(getActivity());
        startActivity(i);

    }
    //*******************************************************************************************
    //addToLog(long s)
    //      *adds a new session to the SessionLog
    //
    //*******************************************************************************************
    private void addToLog(String s){
        Session mSession = new Session();
        mSession.setDuration(s);

        SessionLog.get(getActivity()).addSession(mSession);
    }
    //*******************************************************************************************
    //findImage()
    //      *creates a new location request
    //      *adds the current location to map
    //      *adjust the camera to the current location
    //
    //*******************************************************************************************
    private void findImage(){
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        request.setInterval(5000);
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "START LOCATION: " + location);
                        LatLng myPoint = new LatLng(
                                location.getLatitude(), location.getLongitude());
                        mCurrentLocation = location;
                        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap(mMapImage);

                        MarkerOptions myMarker = new MarkerOptions()
                                .position(myPoint);
                        mMap.clear();
                        mMap.addMarker(myMarker);

                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myPoint, 15);
                        mMap.moveCamera(update);
                        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
                    }

                });
    }
    //*******************************************************************************************
    //findImageDriving()
    //      *creates a new location request
    //      *adds the current location to map BUT GREEN
    //      *adjust the camera to the current location
    //
    //*******************************************************************************************
    private void findImageDriving(){

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setInterval(10000);
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    int strike = 0;
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i(TAG, "Got a fix: " + location);
                        LatLng myPoint = new LatLng(
                                location.getLatitude(), location.getLongitude());
                        mNewLocation = location;

                        if(mNewLocation.getLongitude() == mCurrentLocation.getLongitude()){
                            //LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
                            //stopDriving();
                            strike++;
                            if(strike == 2) {
                                LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
                                stopDriving(); }
                        } else {
                            mCurrentLocation = mNewLocation;
                        }
                        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap(mMapImage);

                        MarkerOptions myMarker = new MarkerOptions()
                                .position(myPoint).icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                        mMap.clear();
                        mMap.addMarker(myMarker);

                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myPoint, 15);
                        mMap.moveCamera(update);
                    }
                });
    }



}
