package com.tcs.skillbiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Communicator {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView NickName, topicsCount, badgeCount;
    private ProgressBar navProgressBar;
    private ImageView icon;
    public  static int Emp_id;
    public static String Nick_name;
    public static int Avatar;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = new Database(this);

        Emp_id=getIntent().getIntExtra("Emp_id", 000000);
        Nick_name=getIntent().getStringExtra("NickName");
        Avatar=getIntent().getIntExtra("Avatar", 00000);

        if(SplashScreen.firstTime) {

        }

        setupNavigationDrawer();

        //Calling Topics Fragment
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_vertical, R.anim.exit_vertical, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.contentLayout, new TopicsFragment()).commit();
        }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { // <---- added
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState(); // important statetment for drawer to
    }


    public void setupNavigationDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        {
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
                //drawerOpened = false;
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

                Cursor cursor = database.getAllDataFromTable(Database.DBHelper.TABLE_TOPICS);
                int totalTopicCount = cursor.getCount();
                int completedTopicsCount= database.getCountCompletedTopics(MainActivity.Emp_id);

                topicsCount = (TextView)drawerView.findViewById(R.id.topicsCount);
                topicsCount.setText(completedTopicsCount + " / " + totalTopicCount);

                badgeCount = (TextView)drawerView.findViewById(R.id.badgeCount);
                badgeCount.setText(0+"");

                navProgressBar = (ProgressBar)drawerView.findViewById(R.id.navProgressBar);
                navProgressBar.setProgress((completedTopicsCount * 100) / totalTopicCount);


                supportInvalidateOptionsMenu();
                //drawerOpened = true;
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        NickName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.NN);
        NickName.setText(Nick_name);

        icon=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.ico);
        icon.setImageResource(Avatar);


        final LinearLayout logout=(LinearLayout)navigationView.getHeaderView(0).findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "in click");
                final AlertDialog.Builder logoutAlert = new AlertDialog.Builder(MainActivity.this);
                logoutAlert.setCancelable(false);
                logoutAlert.setTitle("LOGOUT");
                logoutAlert.setMessage("ARE YOU SURE YOU WANT TO LOGOUT ...?");
                logoutAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(database.updateLoginStatus(Emp_id,"false") != 0) {
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
                logoutAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog logout= logoutAlert.create();
                logout.show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplashScreen.firstTime=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callBack( TopicBean topicBean) {
//        startActivity(new Intent(this,ContentActivity.class));
        //Calling Content Fragment

        Bundle bundle = new Bundle();
        bundle.putInt("topicId", topicBean.getId());
        bundle.putString("audioURL", topicBean.getAudioURL());
        bundle.putString("videoURL", topicBean.getVideoURL());
        bundle.putInt("quizId", topicBean.getQuizID());

        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.contentLayout,contentFragment).addToBackStack("tag").commit();
    }

}
