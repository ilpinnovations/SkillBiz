package com.tcs.skillbiz;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends android.support.v4.app.Fragment {

    TextView infoText;
    View upLine;
    Button quizAttempt;
    ImageView quizLock,quizUnlock;
    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizLock = (ImageView) view.findViewById(R.id.quizLock);
        quizUnlock = (ImageView) view.findViewById(R.id.quizUnlock);
        quizAttempt= (Button) view.findViewById(R.id.quizAttemptButton);

        upLine = view.findViewById(R.id.upLine);
        infoText = (TextView)view.findViewById(R.id.infoText);

        final Button button = (Button)view.findViewById(R.id.quizAttemptButton);

        // To check videoCompleted when fragment opens for first time also
        onResume();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button temp = (Button)v;

                if(!temp.getText().toString().equalsIgnoreCase("Improve")) {
                    Animation fade_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    Animation fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                    quizLock.startAnimation(fade_out);
                    quizUnlock.startAnimation(fade_in);

                    fade_in.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            getContext().startActivity(new Intent(getContext(), QuizActivity.class));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                else {
                    getContext().startActivity(new Intent(getContext(), QuizActivity.class));
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume ", "Onresume called");
//        message= (TextView) getActivity().findViewById(R.id.message);

        Database database = new Database(getActivity());

        boolean quizFlag = database.isCompleted(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_QUIZ_COMPLETED);
        if(quizFlag) {
            quizAttempt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            quizAttempt.setVisibility(View.VISIBLE);
            quizAttempt.setEnabled(true);
            quizAttempt.setText("Improve");

            quizLock.setVisibility(View.INVISIBLE);
            quizUnlock.setVisibility(View.VISIBLE);

            upLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            infoText.setBackgroundColor(Color.parseColor("#60004D40"));
            infoText.setText("Good to see you again!\n");
        }
        else {

            boolean audioFlag = database.isCompleted(MainActivity.Emp_id, ContentFragment.topicId, Database.DBHelper.COLUMN_IS_AUDIO_COMPLETED);
            boolean videoFlag = database.isCompleted(MainActivity.Emp_id, ContentFragment.topicId, Database.DBHelper.COLUMN_IS_VIDEO_COMPLETED);
            if (videoFlag) {
                quizAttempt.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                quizAttempt.setVisibility(View.VISIBLE);
                quizAttempt.setEnabled(true);

                upLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                infoText.setBackgroundColor(Color.parseColor("#60004D40"));
                infoText.setText("Congratulations!\nUse the Key on to Unlock the Quiz");
            }
        }

    }

}
