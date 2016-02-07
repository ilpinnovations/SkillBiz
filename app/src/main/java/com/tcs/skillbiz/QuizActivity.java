package com.tcs.skillbiz;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {


    CardView option1, option2, option3, option4;
    Animation shakeAnim = null, spinAnim = null;


    ArrayList<CardView> cardList= new ArrayList<CardView>();

    ArrayList<QuizBean> quizBeansList = new ArrayList<>();
    TextView question, qOption1, qOption2, qOption3, qOption4, questionProgress;
    Button result;
    CardView selectedCard;
    ProgressBar progressBar;

    int questionTrack = 0;
    int answerOption = 0;
    int score = 0;
    boolean inCheck = false;
    Animation small_scale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d("tag", "Inside quizA");

        question = (TextView) findViewById(R.id.question);
        qOption1 = (TextView) findViewById(R.id.qOption1);
        qOption2 = (TextView) findViewById(R.id.qOption2);
        qOption3 = (TextView) findViewById(R.id.qOption3);
        qOption4 = (TextView) findViewById(R.id.qOption4);
        questionProgress = (TextView) findViewById(R.id.questionProgress);

        option1 = (CardView) findViewById(R.id.option1);
        option2 = (CardView) findViewById(R.id.option2);
        option3 = (CardView) findViewById(R.id.option3);
        option4 = (CardView) findViewById(R.id.option4);

        cardList.add(option1);
        cardList.add(option2);
        cardList.add(option3);
        cardList.add(option4);

        result = (Button) findViewById(R.id.resultButton);

        small_scale = AnimationUtils.loadAnimation(this, R.anim.small_scale);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e6a422"), PorterDuff.Mode.SRC_IN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#00796B"));
        }



        Database database = new Database(this);
        Cursor cursor = database.getQuizQuestions(ContentFragment.quizId);

        Log.d("SkillBiz",ContentFragment.quizId+"");

        while (cursor.moveToNext()) {
            QuizBean quizBean = new QuizBean(
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_QUIZ_ID)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_QUESTION)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_OPTION1)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_OPTION2)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_OPTION3)),
                    cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_OPTION4)),
                    cursor.getInt(cursor.getColumnIndex(Database.DBHelper.COLUMN_CORRECT_OPTION)));
            Log.d("SkillBIz.cursor","Added Question: "+cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_QUESTION)));
            quizBeansList.add(quizBean);
        }

       /* for (int i = 0; i < 10; i++) {
            QuizBean QuizBean = new QuizBean("1", question + i, option + "1", option + "2", option + "3", option + "4",1);
            quizBeansList.add(QuizBean);
        }*/

        Log.d("tag", "Inside quizA before P");
        populateQuestion(questionTrack);

    }

    private void populateQuestion(int i) {

        if (quizBeansList.size() != 0 && i < quizBeansList.size()) {

            Log.d("tag", "Inside quizA in P");
            QuizBean QuizBean = quizBeansList.get(i);
            question.setText(QuizBean.getQuestion());
            qOption1.setText(QuizBean.getOption1());
            qOption2.setText(QuizBean.getOption2());
            qOption3.setText(QuizBean.getOption3());
            qOption4.setText(QuizBean.getOption4());
            answerOption = QuizBean.getAnswerOption()-1;

            int progress = (Integer)(((i+1)*100) / (quizBeansList.size()));
            Log.d("Progress", progress + "");
            progressBar.setProgress(progress);
            questionProgress.setText((i+1) + " / " + (quizBeansList.size()));

            option1.setOnClickListener(this);
            option2.setOnClickListener(this);
            option3.setOnClickListener(this);
            option4.setOnClickListener(this);

            if(QuizBean.getOption4().equalsIgnoreCase(""))
                option4.setVisibility(View.GONE);
            else
                option4.setVisibility(View.VISIBLE);


            result.setOnClickListener(this);


        } else {
            inCheck=false;

            if(quizBeansList.size()==0) {
                Toast.makeText(this, "Captain! We got in trouble", Toast.LENGTH_SHORT).show();
                finish();
            }

            updateDb();
//          Toast.makeText(this, "Captain! We got in trouble", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Congratulations!\nYou have successfully completed this Topic with score: " + score +"\n\nTap Continue to go to Topics screen");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Database database = new Database(QuizActivity.this);
                    if(database.isCompleted(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_QUIZ_COMPLETED))
                        ContentFragment.tabLayout.getTabAt(2).setIcon(R.drawable.quiz_success96);

                    finish();
                }
            });
            builder.setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    private void updateDb(){
        Database database = new Database(this);
        int success = database.updateUserActivity(MainActivity.Emp_id,ContentFragment.topicId, Database.DBHelper.COLUMN_IS_QUIZ_COMPLETED, "TRUE");
        int success1 = database.updateUserActivity(MainActivity.Emp_id, ContentFragment.topicId, Database.DBHelper.COLUMN_SCORES, score + "");

        int success2 = database.updateUserActivity(MainActivity.Emp_id, ContentFragment.topicId, Database.DBHelper.COLUMN_SCORES_UPDATE_TIME, "");
        Log.d("SkillBiz","inQUiz success2: "+success2);

        Cursor cursor = database.getAllDataFromTable(Database.DBHelper.TABLE_USER_ACTIVITY);

        while(cursor.moveToNext()){
            Log.d("SkillBiz","Quiz UpdateDB TimeStamp"+cursor.getString(cursor.getColumnIndex(Database.DBHelper.COLUMN_SCORES_UPDATE_TIME)));
        }


        if(success == 0 || success1 ==0) {
            Toast.makeText(this,"QUIZ COMPLETION Update Problem!",Toast.LENGTH_SHORT).show();
        }


        cursor = database.leaderBoardRetrieve();

        Log.d("SkillBiz","Cursor1 Size: "+cursor.getCount());

        while (cursor.moveToNext()) {
            Log.d("SkillBiz","LeaderBoardTag: "+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2));
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof CardView) {
            //Toast.makeText(QuizActivity.this, "Click on CardView", Toast.LENGTH_SHORT).show();
            changeCards((CardView) v);
            if(!result.isEnabled()) {
                enableResultButton();
            }
        }

        if (v instanceof Button) {

            String temp = ((Button) v).getText().toString();
            Log.d("bTag",temp);
            if(temp.equals(getResources().getString(R.string.checkButtonMessage))) {
                inCheck = true;
                if (selectedCard != cardList.get(answerOption)) {
                    selectedCard.setCardBackgroundColor(Color.parseColor("#ed79080d"));
                }
                else {
                    score++;
                }
                cardList.get(answerOption).setCardBackgroundColor(Color.parseColor("#e22a7414"));
                ((Button) v).setBackgroundColor(Color.parseColor("#dc1b36be"));
                ((Button) v).setTextColor(Color.parseColor("#cfc8c8"));
                ((Button) v).setText(R.string.nextButtonMessage);
            }

            if(temp.equals(getResources().getString(R.string.nextButtonMessage))) {
                inCheck = false;
                Log.d("inbTag",temp);
                reset();
                populateQuestion(++questionTrack);
            }
        }
    }

    private void changeCards(CardView cardView) {

        if(!inCheck) {
            for (CardView itr : cardList) {
                if (cardView == itr) {
                    cardView.setCardBackgroundColor(Color.parseColor("#0451a4"));
                    cardView.startAnimation(small_scale);
                    cardView.setCardElevation(16);
                    selectedCard = itr;
                    Log.d("tagK", selectedCard + "");
                } else {
                    itr.setCardBackgroundColor(Color.parseColor("#60E8EAF6"));
                    itr.setCardElevation(2);
                }
            }
        }
    }

    private void enableResultButton() {
            //result.setBackgroundColor(Color.parseColor("#dc075c1a"));
            result.setText(R.string.checkButtonMessage);
            result.setEnabled(true);
        }

    private void reset() {
        for(CardView itr:cardList) {
                itr.setCardBackgroundColor(Color.parseColor("#60E8EAF6"));
                itr.setCardElevation(2);
            }
        result.setEnabled(false);
        result.setBackgroundColor(Color.parseColor("#FFB300"));
        result.setTextColor(Color.parseColor("#565353"));
        result.setText(R.string.disabledButtonMessage);
        selectedCard=null;
    }
}