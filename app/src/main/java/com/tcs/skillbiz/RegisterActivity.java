package com.tcs.skillbiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
    private int Avatar;
    private Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database=new Database(this);
        Avatar= R.drawable.userfemale;
    }

    public void onAvathar(View v){
        if(v.getId()== R.id.male){
            Avatar= R.drawable.user_icon;
            ((ImageView)findViewById(R.id.female)).setAlpha((float) 0.25);
            ((ImageView)findViewById(R.id.female)).setBackgroundResource(0);

        }
        else{
            Avatar= R.drawable.userfemale;
            ((ImageView)findViewById(R.id.male)).setAlpha((float) 0.25);
            ((ImageView)findViewById(R.id.male)).setBackgroundResource(0);
        }
        v.setAlpha(1);
        v.setBackgroundResource(R.drawable.avatharbackground);
    }
    public void Register(View view){
        int empid=Integer.parseInt(((EditText) findViewById(R.id.emp_id)).getText().toString());
        String passsword=((EditText)findViewById(R.id.password)).getText().toString();
        String nick_name=((EditText)findViewById(R.id.nickname)).getText().toString();
        if(database.insertLogin(nick_name,empid,passsword,Avatar,"true")!=-1) {

            // Add default data to UserActivity Table
            Cursor cursor = database.getAllDataFromTable(Database.DBHelper.TABLE_TOPICS);
            Log.d("SkillBiz","InRegister Cursor count: "+cursor.getCount()+"");
            while (cursor.moveToNext()) {
                if(database.insertUserActivity(empid,
                        cursor.getInt(cursor.getColumnIndex(Database.DBHelper.COLUMN_TOPIC_ID))) == -1)
                {
                    Log.d("SkillBiz","InRegister Topic ID: "+cursor.getInt(cursor.getColumnIndex(Database.DBHelper.COLUMN_TOPIC_ID))+"");
                }
                else {
                    Log.d("SkillBiz","InRegister Topic ID: "+cursor.getInt(cursor.getColumnIndex(Database.DBHelper.COLUMN_TOPIC_ID))+"");
                }
            }

            Intent i=new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();

        }
        else
            Toast.makeText(this,"USER ALREADY EXISTS",Toast.LENGTH_LONG).show();

    }
}
