package com.tcs.skillbiz;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends android.support.v4.app.Fragment {

    ImageView play;
    VideoView video;
    //ViewPager viewPager;
    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        play=(ImageView)getActivity().findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                play.setVisibility(View.INVISIBLE);
                Intent i = new Intent(getContext(), VideoActivity.class);
                startActivityForResult(i, 1);

            }
        });

        Database database = new Database(getActivity());
        if(database.isCompleted(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_VIDEO_COMPLETED))
        ContentFragment.tabLayout.getTabAt(1).setIcon(R.drawable.video_success96);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        play.setVisibility(View.VISIBLE);
    }
}
