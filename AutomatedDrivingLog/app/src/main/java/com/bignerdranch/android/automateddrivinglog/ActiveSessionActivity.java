package com.bignerdranch.android.automateddrivinglog;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class ActiveSessionActivity extends SingleFragmentActivityMap {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ActiveSessionActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        //return new ActiveSessionFragment();
        return new MapViewFragment();
    }
}
