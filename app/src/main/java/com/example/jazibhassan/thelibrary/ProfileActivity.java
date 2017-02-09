package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView;

public class ProfileActivity extends Activity {

    TextView e_userid,
            e_password,
            e_confirmpass,
            e_email,
            e_name,
            e_address,
            e_contact,
            e_telno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        init2();


    }


    void init() {
        e_userid = (TextView) findViewById(R.id.sedit1);
        e_password = (TextView) findViewById(R.id.sedit2);
        e_confirmpass = (TextView) findViewById(R.id.sedit3);
        e_email = (TextView) findViewById(R.id.sedit4);
        e_name = (TextView) findViewById(R.id.sedit5);
        e_address = (TextView) findViewById(R.id.sedit6);
        e_contact = (TextView) findViewById(R.id.sedit7);
        e_telno = (TextView) findViewById(R.id.sedit8);


    }

    void init2() {

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREF_USER, MODE_PRIVATE);


            if(sharedPreferences.getBoolean(MainActivity.USER_LOGGEDIN,false)) {
                e_userid.setText(sharedPreferences.getString(MainActivity.USER_ID,"N/A"));
                e_email.setText(sharedPreferences.getString(MainActivity.USER_EMAIL, "N/A"));
                e_name.setText(sharedPreferences.getString(MainActivity.USER_NAME, "N/A"));
                e_address.setText(sharedPreferences.getString(MainActivity.USER_ADDRESS, "N/A"));
                e_contact.setText(sharedPreferences.getString(MainActivity.USER_CONTACT, "N/A"));
                e_telno.setText(sharedPreferences.getString(MainActivity.USER_TELNUM, "N/A"));
            }

    }
}
