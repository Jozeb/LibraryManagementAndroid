package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jazibhassan.thelibrary.dbpackage.DbSource;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    public static String PREF_USER = "user_lol";

    public static String USER_ID = "userid";
    public static String USER_NAME = "username";
    public static String USER_CONTACT = "usercontact";
    public static String USER_ADDRESS = "useraddress";
    public static String USER_EMAIL = "useremail";
    public static String USER_TELNUM = "usertelno";
    public static String USER_FNAME = "userfname";
    public static String USER_LOGGEDIN = "userlogin";


    public static String IP = "http://192.168.173.1/";

    SharedPreferences sharedPreferences;

    Button Login,
            Search,
            Request,
            Profile,
            History,
            Issue;

    ImageButton Notify;
    Typeface typeface;

    public static DbSource database;


    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        database = new DbSource(this);
        Login = (Button) findViewById(R.id.option_login);
        Login.setTypeface(typeface);

        Search = (Button) findViewById(R.id.option_search);
        Search.setTypeface(typeface);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        Request = (Button) findViewById(R.id.option_request);
        Request.setTypeface(typeface);
        Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRequestActivity.class);
                startActivity(intent);
            }
        });

        Profile = (Button) findViewById(R.id.option_profile);
        Profile.setTypeface(typeface);
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        History = (Button) findViewById(R.id.option_history);
        History.setTypeface(typeface);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        Issue = (Button) findViewById(R.id.option_issue);
        Issue.setTypeface(typeface);
        Issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IssueActivity.class);
                startActivity(intent);
            }
        });

        Notify = (ImageButton) findViewById(R.id.option_noti);
        Notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        TextView title = (TextView) findViewById(R.id.title1);
        title.setTypeface(typeface);

//        new task().execute();

      //  timer = new Timer();



       /* TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run () {
                Intent intent = new Intent(MainActivity.this, NotificationService.class);
                startService(intent);
                Log.i("alright", "repeat");
                // your code here...
            }
        };


// schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0l, 2000);*/
        try {
            Intent intent = new Intent(this, NotificationService.class);
            PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(this, NotificationService.class);
            PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pi); // Millisec * Second * Minute

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        sharedPreferences = getSharedPreferences(PREF_USER, MODE_PRIVATE);

        /*timer.cancel();
        timer = new Timer();

        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                if (mWifi.isConnected()) {
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);
                    startService(intent);
                    Log.i("alright", "repeat");
                }

                // your code here...
            }
        };

// schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0l, 5000);*/

        if (!sharedPreferences.getBoolean(USER_LOGGEDIN, false)) {
            Profile.setEnabled(false);
            Request.setEnabled(false);
            Issue.setEnabled(false);
            History.setEnabled(false);

            Profile.setBackgroundResource(R.drawable.button_false);
            Request.setBackgroundResource(R.drawable.button_false);
            Issue.setBackgroundResource(R.drawable.button_false);
            History.setBackgroundResource(R.drawable.button_false);
            Login.setText("Login");

            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            });
        } else {

            Profile.setEnabled(true);
            Request.setEnabled(true);
            Issue.setEnabled(true);
            History.setEnabled(true);
            Profile.setBackgroundResource(R.drawable.button2_selector);
            Request.setBackgroundResource(R.drawable.button2_selector);
            Issue.setBackgroundResource(R.drawable.button2_selector);
            History.setBackgroundResource(R.drawable.button2_selector);

            Login.setText("Signout");

            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(USER_LOGGEDIN, false);
                    editor.putString(USER_ID, "N/A");
                    Login.setText("Login");

                    database.delete_noti_data();
                    database.delete_hist_data();
                    database.delete_issue_data();

                    Profile.setEnabled(false);
                    Request.setEnabled(false);
                    Issue.setEnabled(false);
                    History.setEnabled(false);
                    Profile.setBackgroundResource(R.drawable.button_false);
                    Request.setBackgroundResource(R.drawable.button_false);
                    Issue.setBackgroundResource(R.drawable.button_false);
                    History.setBackgroundResource(R.drawable.button_false);
                    editor.apply();

                    Login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }
                    });

                }
            });
        }
        super.onResume();
    }

   /* @Override
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
