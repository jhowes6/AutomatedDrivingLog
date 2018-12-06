package com.bignerdranch.android.automateddrivinglog;

import android.media.MediaCas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by howes on 2/10/2018.
 */

public class LogFragment extends Fragment {

    private RecyclerView mSessionRecyclerView;
    private SessionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_list, container, false);
        mSessionRecyclerView = (RecyclerView) view
                .findViewById(R.id.session_recycler_view);
        mSessionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }
    //********************************************************************************************
    //updateUI()
    //********************************************************************************************
    private void updateUI(){
        SessionLog sessionLog = SessionLog.get(getActivity());
        List<Session> sessions = sessionLog.getSessions();

        if(mAdapter == null) {
            mAdapter = new SessionAdapter(sessions);
            mSessionRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSessions(sessions);
            mAdapter.notifyDataSetChanged();
        }
    }
    //********************************************************************************************
    //SessionHolder class
    //********************************************************************************************
    private class SessionHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
        private Session mSession;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mDurationTextView;

        public void bindSession(Session session){
            mSession = session;
            mTitleTextView.setText(mSession.getTitle());
            mDateTextView.setText(mSession.getDate().toString());
            mDurationTextView.setText(mSession.getDuration());
        }
        public SessionHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_session_title_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_session_date_text_view);
            mDurationTextView = (TextView)
                    itemView.findViewById(R.id.list_item_duration_text_view);
        }
        @Override
        public void onClick(View v){

        }
    }
    //********************************************************************************************
    //CrimeAdapter class
    //********************************************************************************************
    private class SessionAdapter extends RecyclerView.Adapter<SessionHolder> {
        private List<Session> mSessions;
        public SessionAdapter(List<Session> sessions) {
            mSessions = sessions;
        }
        @Override
        public SessionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_session, parent, false);
            return new SessionHolder(view);
        }
        @Override
        public void onBindViewHolder(SessionHolder holder, int position) {
            Session session = mSessions.get(position);
            holder.bindSession(session);
        }
        @Override
        public int getItemCount(){
            return mSessions.size();
        }

        public void setSessions(List<Session> sessions) {
            mSessions = sessions;
        }
    }
}
