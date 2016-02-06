package com.tcs.skillbiz;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView v= (VideoView) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(v);
        v.setMediaController(mediaController);
        v.setKeepScreenOn(true);
        Log.d("SkillBiz",ContentFragment.videoURL);


        if(ContentFragment.videoURL.equals("")) {
            v.setVideoPath("android.resource://com.tcs.skillbiz/" + R.raw.k);
            Toast.makeText(VideoActivity.this, "URL is not there yet!", Toast.LENGTH_LONG).show();}
        else {
            if (isNetworkAvailable()) {
//                String vidAddress = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";
                String vidAddress = ContentFragment.videoURL;
                Uri vidUri = Uri.parse(vidAddress);
                v.setVideoURI(vidUri);
                Toast.makeText(VideoActivity.this, "Network Available!\nVideo will buffer from Internet", Toast.LENGTH_LONG).show();
            } else {
                v.setVideoPath("android.resource://com.tcs.skillbiz/" + R.raw.k);
                Toast.makeText(VideoActivity.this, "No Network Available!\nVideo will play from Device", Toast.LENGTH_LONG).show();
            }
        }

            v.start();
            v.requestFocus();


            v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    updateDb();
                    Toast.makeText(VideoActivity.this,"Video onCompletion Called",Toast.LENGTH_SHORT).show();

//                    ContentFragment.tabLayout.getTabAt(ContentFragment.tabLayout.getSelectedTabPosition()).setIcon(R.drawable.audio_done2_96);
////                    ContentFragment.tabLayout.getTabAt(0).setIcon(R.drawable.audio_done96);
////                    ContentFragment.tabLayout.getTabAt(2).setIcon(R.drawable.audio_done96);
                    finish();
                }
            });
        }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void updateDb(){
        Database database = new Database(this);
        int success = database.updateUserActivity(MainActivity.Emp_id, ContentFragment.topicId, Database.DBHelper.COLUMN_IS_VIDEO_COMPLETED,"TRUE" );
        if(success == 0) {
            Toast.makeText(this,"VIDEO COMPLETION Update Problem!",Toast.LENGTH_SHORT).show();
        }
    }


}
