package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddRequestActivity extends Activity {

    EditText title, author, edition;
    Button addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        title = (EditText) findViewById(R.id.add_title);
        author = (EditText) findViewById(R.id.add_author);
        edition = (EditText) findViewById(R.id.add_edition);
        addbtn = (Button) findViewById(R.id.add_btn);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREF_USER,MODE_PRIVATE);
        final String userid = sharedPreferences.getString(MainActivity.USER_ID,"N/A");


       addbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String atitle = title.getText().toString();
               String aauthor = author.getText().toString();
               String aedition = edition.getText().toString();
               if(userid.isEmpty() || userid.compareTo("N/A")==0){
                   Toast.makeText(AddRequestActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
               }else{
                   new AddRequestTask().execute(userid,atitle,aauthor,aedition);
               }


           }
       });

    }

    class AddRequestTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {
            String sign_url = MainActivity.IP+"library/add_brequest.php";


            try {
                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                //new SignupTask().execute(userid,password,email,name,address,contact,tellno);
                String data = URLEncoder.encode("userid", "UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+"&"+
                        URLEncoder.encode("book_title","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"+
                        URLEncoder.encode("book_author","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+"&"+
                        URLEncoder.encode("book_edition","UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                return "Success";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Akhtar", "Signup Task Finished");

            if(s.compareTo("Success")==0){
                finish();
            }else{
                Toast.makeText(AddRequestActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
