package com.tcs.skillbiz;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopicsFragment extends Fragment {


    public TopicsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<TopicBean> topicBeanList = new ArrayList<>();

        Database database = new Database(getActivity());
        Cursor cursor = database.getAllDataFromTable(Database.DBHelper.TABLE_TOPICS);
        while(cursor.moveToNext()) {

            TopicBean topicBean = new TopicBean(
                    cursor.getInt(cursor.getColumnIndex(Database.DBHelper.COLUMN_TOPIC_ID)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_TOPIC_NAME)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_AUDIO_URL)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_VIDEO_URL)),
                    cursor.getInt(cursor.getColumnIndex(Database.DBHelper.COLUMN_QUIZ_ID)));

            topicBeanList.add(topicBean);
        }

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.topicList);
        RecycleAdapter recycleAdapter = new RecycleAdapter(getActivity(), topicBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recycleAdapter);

    }
}
