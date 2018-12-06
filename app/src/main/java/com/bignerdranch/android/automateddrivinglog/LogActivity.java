package com.bignerdranch.android.automateddrivinglog;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LogActivity.class);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return new LogFragment();
    }
}
