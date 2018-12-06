package com.bignerdranch.android.automateddrivinglog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity  extends AppCompatActivity{

    private static final int REQUEST_CODE_DRIVE = 0;
    private static final int REQUEST_CODE_LOG = 1;
    public  static int sessionCount = 0;

    private Button mStartSessionButton;
    private Button mViewLogButton;
    public static int incrementSessionCount(){
        sessionCount++;
        return sessionCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartSessionButton = (Button) findViewById(R.id.start_session_button);
        mStartSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch Active Session activity
                Intent i = ActiveSessionActivity.newIntent(MainActivity.this);
                startActivityForResult(i, REQUEST_CODE_DRIVE);
            }
        });
        mViewLogButton = (Button) findViewById(R.id.view_log_button);
        mViewLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch driving log activity
                Intent i = LogActivity.newIntent(MainActivity.this);
                startActivityForResult(i, REQUEST_CODE_LOG);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CODE_DRIVE) {
            if(data == null) {
                return;
            }
        }
    }

}
