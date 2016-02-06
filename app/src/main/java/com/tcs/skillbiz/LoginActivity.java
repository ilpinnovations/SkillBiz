package com.tcs.skillbiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
     private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = new Database(this);
        Cursor c=database.isUserLogin();
        if(c!= null && c.moveToNext()){

            Intent i=new Intent(this,MainActivity.class);
            i.putExtra("Emp_id",c.getInt(c.getColumnIndex("EMP_ID")));
            i.putExtra("NickName",c.getString(c.getColumnIndex("NICK_NAME")));
            i.putExtra("Avatar",c.getInt(c.getColumnIndex("AVATAR")));
            startActivity(i);
            finish();
        }

        if(SplashScreen.firstTime) {
            initializeData(database);
        }

    }
    public void login(View v){
        EditText e=(EditText)findViewById(R.id.emp_id);
        int Emp_id=Integer.parseInt(e.getText().toString());
        e=(EditText)findViewById(R.id.password);
        String Password=e.getText().toString();
        Cursor c=database.validateLogin(Emp_id, Password);
        if(c!=null && c.moveToNext()){
            if(database.updateLoginStatus(Emp_id,"true") != 0) {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("Emp_id", c.getInt(c.getColumnIndex("EMP_ID")));
                i.putExtra("NickName", c.getString(c.getColumnIndex("NICK_NAME")));
                i.putExtra("Avatar", c.getInt(c.getColumnIndex("AVATAR")));
                startActivity(i);
                finish();
            }
        }
        else
            Toast.makeText(this,"INVALID CREDENTIALS",Toast.LENGTH_LONG).show();
    }

    public void New_User(View view){
        Intent i= new Intent(this,RegisterActivity.class);
        startActivity(i);
        finish();
    }

    // Pre Loaded data to be used in the app demo
    public void initializeData(Database database) {

        Log.d("SkillBiz","Initialize data called");

        database.insertQuiz(1, "Which one do you think is more important ?", "Speaking", "Writing", " Both", "", 3);
        database.insertQuiz(1, "When it comes to immediate response, which mode should be considered ?", "Speaking", "Writing", " None of the above", "", 1);
        database.insertQuiz(1, "What is the advantage of speaking over writing ?", "Fine tuning", "Presence", "Para-language", "All of the above", 4);
        database.insertQuiz(1, "Which mode requires more time for clarification ?", "Speaking", "Writing", " Both", "", 2);
        database.insertQuiz(1, "What is the main advantage of writing over speaking ?", "Think and write", "Spelling errors can be checked", "Easy to convey", "Written proof ", 4);
        database.insertQuiz(1, "When we have to convey a long list of updates, which mode is better ?", "Emails (Writing)", "Telephone (Speaking)", " None of the above", "", 1);

        database.insertQuiz(2, "What is the main advantage of writing over speaking ?", "Think and write", "Spelling errors can be checked", "Easy to convey", "Written proof ", 4);
        database.insertQuiz(2, "When we have to convey a long list of updates, which mode is better ?", "Emails (Writing)", "Telephone (Speaking)", " None of the above", "", 1);
        database.insertQuiz(2, "Which one do you think is more important ?", "Speaking", "Writing", " Both", "", 3);
        database.insertQuiz(2, "What is the advantage of speaking over writing ?", "Fine tuning", "Presence", "Para-language", "All of the above", 4);
        database.insertQuiz(2, "When it comes to immediate response, which mode should be considered ?", "Speaking", "Writing", " None of the above", "", 1);

        database.insertQuiz(3, "Which one do you think is more important ?", "Speaking", "Writing", " Both", "", 3);
        database.insertQuiz(3, "What is the main advantage of writing over speaking ?", "Think and write", "Spelling errors can be checked", "Easy to convey", "Written proof ", 4);

        database.insertQuiz(4, "When it comes to immediate response, which mode should be considered ?", "Speaking", "Writing", " None of the above", "", 1);
        database.insertQuiz(4, "Which one do you think is more important ?", "Speaking", "Writing", " Both", "", 3);
        database.insertQuiz(4, "When we have to convey a long list of updates, which mode is better ?", "Emails (Writing)", "Telephone (Speaking)", " None of the above", "", 1);
        database.insertQuiz(4, "What is the main advantage of writing over speaking ?", "Think and write", "Spelling errors can be checked", "Easy to convey", "Written proof ", 4);

        database.insertQuiz(5, "Which one do you think is more important ?", "Speaking", "Writing", " Both", "", 3);
        database.insertQuiz(5, "When we have to convey a long list of updates, which mode is better ?", "Emails (Writing)", "Telephone (Speaking)", " None of the above", "", 1);
        database.insertQuiz(5, "When it comes to immediate response, which mode should be considered ?", "Speaking", "Writing", " None of the above", "", 1);
        database.insertQuiz(5, "What is the main advantage of writing over speaking ?", "Think and write", "Spelling errors can be checked", "Easy to convey", "Written proof ", 4);

        database.insertTopic("To Speak or to Write","","", 1);
        database.insertTopic("Word Bombs", "", "", 2);
        database.insertTopic("Speak Freak", "", "", 3);
        database.insertTopic("Bright Write", "", "", 4);
        database.insertTopic("Fire to Aspire", "", "", 5);

        SplashScreen.firstTime=false;
    }


}
