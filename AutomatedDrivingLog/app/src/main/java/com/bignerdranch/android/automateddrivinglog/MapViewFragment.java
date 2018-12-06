package com.bignerdranch.android.automateddrivinglog;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by howes on 2/21/2018.
 */

public class MapViewFragment extends SupportMapFragment {
    private GoogleApiClient mClient;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);

        MenuItem startItem = menu.findItem(R.id.action_locate);
        startItem.setEnabled(true);
    }


}
