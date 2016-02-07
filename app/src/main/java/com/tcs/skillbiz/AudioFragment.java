package com.tcs.skillbiz;


import android.app.Fragment;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends android.support.v4.app.Fragment {

    private MediaPlayer mMediaPlayer=null;
    private VisualizerView mVisualiserView = null;
    private Visualizer mVisualizer=null;
    FloatingActionButton floatingActionButton;

    public AudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVisualiserView = (VisualizerView)view.findViewById(R.id.myVisualizerView);

        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mMediaPlayer == null) {
                    initAudio();
                    floatingActionButton.setBackgroundColor(Color.parseColor("#00ff00"));
                    floatingActionButton.setImageResource(android.R.drawable.ic_media_pause);
                    mVisualizer.setEnabled(true);
                } else if (mMediaPlayer.isPlaying()) {
                    floatingActionButton.setImageResource(android.R.drawable.ic_media_play);
                    mMediaPlayer.pause();
                    mVisualizer.setEnabled(false);
                } else {
                    mMediaPlayer.start();
                    floatingActionButton.setImageResource(android.R.drawable.ic_media_pause);
                    mVisualizer.setEnabled(true);
                }
            }
        });

        Database database = new Database(getActivity());
        if(database.isCompleted(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_AUDIO_COMPLETED))
            ContentFragment.tabLayout.getTabAt(0).setIcon(R.drawable.audio_success96);


    }

    private void updateDb(){
        Database database = new Database(getActivity());
        int success = database.updateUserActivity(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_AUDIO_COMPLETED, "TRUE");
        if(success == 0) {
            Toast.makeText(getActivity(),"AUDIO COMPLETION Update Problem!",Toast.LENGTH_SHORT).show();
        }
    }

    private void reset() {
        mMediaPlayer.stop();
        floatingActionButton.setImageResource(android.R.drawable.ic_media_play);
        mVisualizer.release();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    private void initAudio() {
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if(!ContentFragment.audioURL.equals("")) {
            String audioAddress = ContentFragment.audioURL;
            Uri audioUri = Uri.parse(audioAddress);
            mMediaPlayer = MediaPlayer.create(getActivity(),audioUri);
        }
        else {
            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sample);
        }

        setupVisualizerFxAndUI();
        mVisualizer.setEnabled(true);
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mVisualizer.setEnabled(false);
                    }
                });
        mMediaPlayer.start();


        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                updateDb();
                floatingActionButton.setImageResource(android.R.drawable.ic_media_play);
                Database database = new Database(getActivity());
                if(database.isCompleted(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_AUDIO_COMPLETED))
                    ContentFragment.tabLayout.getTabAt(0).setIcon(R.drawable.audio_success96);


//              Toast.makeText(getActivity(),"Audio onCompletion Called",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualiserView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);


    }

    @Override
    public void onPause() {
        super.onPause();

        if(mMediaPlayer != null) {
            reset();
        }
    }
}
