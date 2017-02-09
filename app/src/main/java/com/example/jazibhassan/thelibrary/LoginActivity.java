package com.example.jazibhassan.thelibrary;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends Activity {
    Button Login;

    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(typeface);
        EditText id = (EditText) findViewById(R.id.login_id_t);
        id.setTypeface(typeface);
        EditText pass = (EditText) findViewById(R.id.login_pass_t);
        pass.setTypeface(typeface);

        Button Signup = (Button) findViewById(R.id.signup);
        Signup.setTypeface(typeface);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        Login = (Button) findViewById(R.id.login);
        Login.setTypeface(typeface);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText id = (EditText) findViewById(R.id.login_id_t);
                EditText pass = (EditText) findViewById(R.id.login_pass_t);

                String userid = id.getText().toString();
                String password = pass.getText().toString();
                Login.setEnabled(false);
                new LoginTask(LoginActivity.this).execute(userid, password);

            }
        });
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        Context context;

        public LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String sign_url = MainActivity.IP+"library/get_uinfo.php";


            try {
                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


                String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = " ";

                while ((line = bufferedReader.readLine()) != null) {
                    Log.i("Akhtar2", line);
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.i("Akhtar3", response + "");
                return SetUpUser(response, params[0]);


            } catch (Exception e) {
                //Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return "error";
        }

        private String SetUpUser(String data, String userid) {
            Log.i("Akhtar3", "Set up User");

            String[] columns = data.split("feg");


            //select user_name, user_contact, user_address,
            //user_email, user_telnum, user_fname
            if (columns.length > 2) {
                Log.i("Akhtar","\""+columns[0]+"\"");
                if(columns[0].compareTo("Pending")!=0) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PREF_USER, MODE_APPEND);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(MainActivity.USER_ID, userid);
                    editor.putString(MainActivity.USER_NAME, columns[0]);
                    editor.putString(MainActivity.USER_CONTACT, columns[1]);
                    editor.putString(MainActivity.USER_ADDRESS, columns[2]);
                    editor.putString(MainActivity.USER_EMAIL, columns[3]);
                    editor.putString(MainActivity.USER_TELNUM, columns[4]);
                    editor.putString(MainActivity.USER_FNAME, columns[5]);
                    editor.putBoolean(MainActivity.USER_LOGGEDIN, true);

                    editor.apply();
                    return "true";
                }else{
                    return "pending";
                }
            } else {

                return "false";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Akhtar", "Login Task Finished");
            if (s.compareTo("true")==0) {
                finish();
            }else if(s.compareTo("false")==0){
                Toast.makeText(context, "Invalid User/Pass", Toast.LENGTH_SHORT).show();
            }else if(s.compareTo("pending")==0){
                Toast.makeText(context, "Account Request Pending", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
            Login.setEnabled(true);

        }
    }
}
