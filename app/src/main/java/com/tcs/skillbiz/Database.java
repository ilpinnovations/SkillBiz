package com.tcs.skillbiz;

/**
 * Created by 1040392 on 2/2/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Database {
    Context context;
    DBHelper db;

    public Database(Context context){
        this.context=context;
        db=new DBHelper(context);
    }

    public long insertLogin(String nickname,int emp_id,String password,int avatar,String islogin){
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DBHelper.COLUMN_NICKNAME,nickname);
        cv.put(DBHelper.COLUMN_EMP_ID,emp_id);
        cv.put(DBHelper.COLUMN_PASSWORD, password);
        cv.put(DBHelper.COLUMN_AVATAR, avatar);
        cv.put(DBHelper.COLUMN_IS_LOGIN, islogin);
        long i=database.insert(DBHelper.TABLE_LOGIN, null, cv);
        database.close();
        return i;
    }

    public long insertTopic(String topicName,String audioURL,String videoURL,int quizId){
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DBHelper.COLUMN_TOPIC_NAME,topicName);
        cv.put(DBHelper.COLUMN_AUDIO_URL, audioURL);
        cv.put(DBHelper.COLUMN_VIDEO_URL, videoURL);
        cv.put(DBHelper.COLUMN_QUIZ_ID, quizId);
        long i=database.insert(DBHelper.TABLE_TOPICS, null, cv);
        database.close();
        return i;
    }

    public long insertQuiz(int quizId,String question,String option1,String option2,String option3,String option4,int correctOption){
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DBHelper.COLUMN_QUIZ_ID,quizId);
        cv.put(DBHelper.COLUMN_QUESTION,question);
        cv.put(DBHelper.COLUMN_OPTION1, option1);
        cv.put(DBHelper.COLUMN_OPTION2, option2);
        cv.put(DBHelper.COLUMN_OPTION3, option3);
        cv.put(DBHelper.COLUMN_OPTION4, option4);
        cv.put(DBHelper.COLUMN_CORRECT_OPTION, correctOption);
        long i=database.insert(DBHelper.TABLE_QUIZ, null, cv);
        database.close();
        return i;
    }

    public long insertUserActivity(int empID,int topicId){
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DBHelper.COLUMN_EMP_ID,empID);
        cv.put(DBHelper.COLUMN_TOPIC_ID,topicId);
//        cv.put(DBHelper.COLUMN_IS_AUDIO_COMPLETED, isAudioCompleted);
//        cv.put(DBHelper.COLUMN_IS_VIDEO_COMPLETED, isVideoCompleted);
//        cv.put(DBHelper.COLUMN_IS_QUIZ_COMPLETED, isQuizCompleted);
//        cv.put(DBHelper.COLUMN_SCORES, scores);
        long i=database.insert(DBHelper.TABLE_USER_ACTIVITY, null, cv);
        database.close();
        return i;
    }



    public Cursor getAllDataFromTable(String TABLE_NAME){
        SQLiteDatabase database=db.getWritableDatabase();
        Cursor c=database.query(TABLE_NAME, null, null, null, null, null, null);
        return c;
    }

    public Cursor getQuizQuestions(int QUIZ_ID){
        String STR_QUIZ_ID = String.valueOf(QUIZ_ID);
        SQLiteDatabase database=db.getWritableDatabase();
        Cursor c=database.query(DBHelper.TABLE_QUIZ, null, DBHelper.COLUMN_QUIZ_ID + "= ?", new String[]{STR_QUIZ_ID}, null, null, null);
        return c;
    }


    public Cursor validateLogin(int emp_id,String password){
        SQLiteDatabase database=db.getWritableDatabase();
        String[] arr={DBHelper.COLUMN_EMP_ID, DBHelper.COLUMN_NICKNAME, DBHelper.COLUMN_AVATAR};
        Cursor c=database.query(DBHelper.TABLE_LOGIN, arr, DBHelper.COLUMN_EMP_ID + "= ? AND " + DBHelper.COLUMN_PASSWORD + "= ?", new String[]{emp_id + "", password}, null, null, null);
        return c;
    }

    public Cursor isUserLogin(){
        SQLiteDatabase database=db.getWritableDatabase();
        String[] arr={DBHelper.COLUMN_EMP_ID, DBHelper.COLUMN_NICKNAME, DBHelper.COLUMN_AVATAR};
        Cursor c=database.query(DBHelper.TABLE_LOGIN, arr, DBHelper.COLUMN_IS_LOGIN + "= ?", new String[]{"true"}, null, null, null);
        return c;
    }

    public int updateLoginStatus(int emp_id, String status){
        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DBHelper.COLUMN_IS_LOGIN,status);
        return database.update(DBHelper.TABLE_LOGIN,cv, DBHelper.COLUMN_EMP_ID+"= ?",new String[]{""+emp_id});
        //database.close();
    }

    public Cursor leaderBoardRetrieve() {

        SQLiteDatabase database=db.getWritableDatabase();
/*
        String query = "SELECT *,( "+
                "SELECT COUNT("+DBHelper.COLUMN_SCORES+ ") AS count "+
                "FROM "+ DBHelper.TABLE_USER_ACTIVITY+ " m2 "+
                "WHERE m1."+DBHelper.COLUMN_SCORES + " = m2."+DBHelper.COLUMN_SCORES+") "+
                "FROM "+DBHelper.TABLE_USER_ACTIVITY + " m1 "+
                "WHERE "+DBHelper.COLUMN_SCORES_UPDATE_TIME +" = (SELECT MAX("+DBHelper.COLUMN_SCORES_UPDATE_TIME+") "+
                "FROM "+ DBHelper.TABLE_USER_ACTIVITY + " m3 "+
                "WHERE m1."+DBHelper.COLUMN_SCORES+" = m3."+DBHelper.COLUMN_SCORES+") "+
                "GROUP BY "+ DBHelper.COLUMN_SCORES;
*/

        String query = "SELECT "+
                DBHelper.COLUMN_EMP_ID+", "+
                "MAX("+DBHelper.COLUMN_SCORES_UPDATE_TIME+") AS updated_timestamp, "+
                "SUM("+DBHelper.COLUMN_SCORES+") AS score_sum "+
                "FROM "+DBHelper.TABLE_USER_ACTIVITY+
                " GROUP BY "+DBHelper.COLUMN_EMP_ID+" "+
                " ORDER BY score_sum DESC, updated_timestamp ASC";

                Log.d("SkillBiz", "LeaderBoard Query String: "+query);

        Cursor cursor = database.rawQuery(query,null);

        return cursor;
    }


    public int updateUserActivity(int EMP_ID, int TOPIC_ID, String COLUMN_NAME, String VALUE){

        String STR_EMP_ID = String.valueOf(EMP_ID);
        String STR_TOPIC_ID = String.valueOf(TOPIC_ID);

        SQLiteDatabase database=db.getWritableDatabase();
        ContentValues cv=new ContentValues();
        if(COLUMN_NAME.equals(DBHelper.COLUMN_SCORES))
            cv.put(COLUMN_NAME, Integer.parseInt(VALUE));
        else if(COLUMN_NAME.equals(DBHelper.COLUMN_SCORES_UPDATE_TIME))
            cv.put(COLUMN_NAME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        else
            cv.put(COLUMN_NAME, "TRUE");

        int j=database.update(DBHelper.TABLE_USER_ACTIVITY,cv,DBHelper.COLUMN_EMP_ID+" = ? AND "+DBHelper.COLUMN_TOPIC_ID+" = ?", new String[]{STR_EMP_ID,STR_TOPIC_ID});



        return j;
    }



    public boolean isCompleted(int EMP_ID, int TOPIC_ID, String COLUMN_NAME) {

            String STR_EMP_ID = String.valueOf(EMP_ID);
            String STR_TOPIC_ID = String.valueOf(TOPIC_ID);

            String[] COLUMN= new String[]{COLUMN_NAME};

            SQLiteDatabase database=db.getWritableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_USER_ACTIVITY,COLUMN,DBHelper.COLUMN_EMP_ID+" = ? AND "+DBHelper.COLUMN_TOPIC_ID+" = ?", new String[]{STR_EMP_ID,STR_TOPIC_ID},null,null,null);

            Log.d("SkillQ",(cursor==null)+"");
            Log.d("SkillQ",cursor.getCount()+"");


        while(cursor.moveToNext()) {
                if(cursor.getString(0).equals("TRUE"))
                    return true;
            }
        return false;
    }

    public int getCountCompletedTopics(int EMP_ID){

        String STR_EMP_ID = String.valueOf(EMP_ID);

        SQLiteDatabase database=db.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_USER_ACTIVITY,
                null,
                DBHelper.COLUMN_EMP_ID + " = ? AND " +
                        DBHelper.COLUMN_IS_QUIZ_COMPLETED+" = ? ",
                new String[]{STR_EMP_ID, "TRUE"},
                null,
                null,
                null);

        return cursor.getCount();
    }

    public class DBHelper extends SQLiteOpenHelper {
       static final String DATABASE = "SkillBiz";
       static final String TABLE_LOGIN = "Login";
       static final int DATABASE_VERSION = 1;
       static final String COLUMN_NICKNAME = "NICK_NAME";
       static final String COLUMN_EMP_ID = "EMP_ID";
       static final String COLUMN_PASSWORD = "PASSWORD";
       static final String COLUMN_AVATAR = "AVATAR";
       static final String COLUMN_IS_LOGIN = "IS_LOGIN";

       static final String TABLE_QUIZ = "Quiz";
       static final String COLUMN_ID = "_ID";
       static final String COLUMN_QUESTION = "QUESTION";
       static final String COLUMN_OPTION1 = "OPTION1";
       static final String COLUMN_OPTION2 = "OPTION2";
       static final String COLUMN_OPTION3 = "OPTION3";
       static final String COLUMN_OPTION4 = "OPTION4";
       static final String COLUMN_CORRECT_OPTION = "RIGHT_OPTION";


       static final String TABLE_TOPICS = "Topics";
       static final String COLUMN_TOPIC_ID = "TOPIC_ID";
       static final String COLUMN_TOPIC_NAME = "TOPIC_NAME";
       static final String COLUMN_AUDIO_URL = "AUDIO_URL";
       static final String COLUMN_VIDEO_URL = "VIDEO_URL";
       static final String COLUMN_QUIZ_ID = "QUIZ_ID";
       static final String QUIZ_RELATION = "FOREIGN KEY("+COLUMN_QUIZ_ID+") REFERENCES "+TABLE_QUIZ+"("+COLUMN_QUIZ_ID+")";

       static final String TABLE_USER_ACTIVITY = "USER_ACTIVITY";
       static final String COLUMN_IS_AUDIO_COMPLETED = "IS_AUDIO_COMPLETED";
       static final String COLUMN_IS_VIDEO_COMPLETED = "IS_VIDEO_COMPLETED";
       static final String COLUMN_IS_QUIZ_COMPLETED = "IS_QUIZ_COMPLETED";
       static final String COLUMN_SCORES = "SCORES";
       static final String COLUMN_SCORES_UPDATE_TIME = "SCORE_UPDATE_TIME";
       static final String USER_RELATION = "FOREIGN KEY("+COLUMN_TOPIC_ID+") REFERENCES "+TABLE_TOPICS+"("+COLUMN_TOPIC_ID+")";

        Context c;

        public DBHelper(Context context) {
            super(context, DATABASE, null, DATABASE_VERSION);
            c = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_LOGIN + " (" +
                    COLUMN_EMP_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NICKNAME + " VARCHAR(255), " +
                    COLUMN_PASSWORD + " VARCHAR(255), " +
                    COLUMN_AVATAR + " INTEGER, " +
                    COLUMN_IS_LOGIN + " VARCHAR(255));");

            db.execSQL("CREATE TABLE " + TABLE_QUIZ + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_QUIZ_ID + "  INTEGER, " +
                    COLUMN_QUESTION + " VARCHAR(255), " +
                    COLUMN_OPTION1 + "  VARCHAR(255), " +
                    COLUMN_OPTION2 + "  VARCHAR(255), " +
                    COLUMN_OPTION3 + "  VARCHAR(255), " +
                    COLUMN_OPTION4 + "  VARCHAR(255), " +
                    COLUMN_CORRECT_OPTION + " INTEGER);");

            db.execSQL("CREATE TABLE " + TABLE_TOPICS + " (" +
                    COLUMN_TOPIC_ID + "   INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TOPIC_NAME + " VARCHAR(255), " +
                    COLUMN_AUDIO_URL + "  VARCHAR(255), " +
                    COLUMN_VIDEO_URL + "  INTEGER, " +
                    COLUMN_QUIZ_ID + "    INTEGER, " +
                    QUIZ_RELATION + ");");

            db.execSQL("CREATE TABLE " + TABLE_USER_ACTIVITY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMP_ID + "           INTEGER, " +
                    COLUMN_TOPIC_ID + "         INTEGER, " +
                    COLUMN_IS_AUDIO_COMPLETED + " VARCHAR(255) DEFAULT 'FALSE', " +
                    COLUMN_IS_VIDEO_COMPLETED + " VARCHAR(255) DEFAULT 'FALSE', " +
                    COLUMN_IS_QUIZ_COMPLETED + "  VARCHAR(255) DEFAULT 'FALSE', " +
                    COLUMN_SCORES + "             REAL DEFAULT 0, " +
                    COLUMN_SCORES_UPDATE_TIME + " DATETIME DEFAULT '"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"', " +
                    USER_RELATION + ");");
            Log.d("SkillBiz", "DB onCreate called!");

            SplashScreen.firstTime = true;

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Toast.makeText(c, "UPGrade CALLED", Toast.LENGTH_LONG).show();
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
//            onCreate(db);
        }
    }
}
