package com.bignerdranch.android.automateddrivinglog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by howes on 2/13/2018.
 */

public class ActiveSessionFragment extends Fragment {

    private Session mSession;
    private EditText mTitleField;
    private Button mSaveSessionButton;

    @Override
    public void onPause() {
        super.onPause();

        //SessionLog.get(getActivity())
        //        .updateSession(mSession);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_active_session, container,
                false);
        mSession = new Session();
        mTitleField = (EditText) view.findViewById(R.id.session_title);

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //-----------------------------------
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                if(charSequence.length() == 0) return;
                else
                    mSession.setTitle(charSequence.toString());
                    */
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //------------------------------------
            }
        });

        mSaveSessionButton = (Button) view.findViewById(R.id.save_session_button);
        mSaveSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int x = MainActivity.incrementSessionCount();
                //String title = "Session " + x;
                //mSession.setTitle(title);
                String s = mTitleField.getText().toString();
                mSession.setDuration(s);
                SessionLog.get(getActivity())
                        .updateSession(mSession);
                SessionLog.get(getActivity()).addSession(mSession);
            }
        });
        return view;
    }
}
