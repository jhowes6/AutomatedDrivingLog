package com.bignerdranch.android.automateddrivinglog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.UUID;

public class ActiveSessionActivity extends SingleFragmentActivity {
    private static final int REQUEST_ERROR = 0;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ActiveSessionActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        //return new ActiveSessionFragment();
        return MapViewFragment.newInstance();
    }
    @Override
    protected void onResume(){
        super.onResume();

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil
                    .getErrorDialog(errorCode, this, REQUEST_ERROR,
                        new DialogInterface.OnCancelListener() {
                        @Override
                            public void onCancel(DialogInterface dialogInterface) {
                            finish();
                        }
                        });
            errorDialog.show();
        }
    }
}
