package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends Activity {

    DrawerLayout drawerLayout;

    ArrayList<BookClass> bookClassArrayList;
    ListView listView;

    EditText search_title;
    EditText search_author;

    Button btn_filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        search_title = (EditText) findViewById(R.id.search_title);
        search_author = (EditText) findViewById(R.id.search_author);

        btn_filter = (Button) findViewById(R.id.search_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = search_title.getText().toString();
                String author = search_author.getText().toString();

                if(!title.isEmpty() || !author.isEmpty()){
                    Log.i("Akhtar","Atleast 1 wasnt empty");
                    new SearchTask(SearchActivity.this).execute(title, author);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

            }
        });


        bookClassArrayList = new ArrayList<BookClass>();

        listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(new SearchAdapter(bookClassArrayList));
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            super.onBackPressed();
        }
    }

    class SearchAdapter extends BaseAdapter{

        ArrayList<BookClass> list;

        public SearchAdapter(ArrayList<BookClass> bookClassArrayList){
            this.list = bookClassArrayList;

        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View r;

            if(view == null){
              r =  getLayoutInflater().inflate(R.layout.bookrow,null);
            }else{
                r= view;
            }

            TextView title = (TextView) r.findViewById(R.id.b_title);
            TextView author = (TextView) r.findViewById(R.id.b_author);
            TextView edition = (TextView) r.findViewById(R.id.b_edition);
            TextView location = (TextView) r.findViewById(R.id.b_location);
            TextView status = (TextView) r.findViewById(R.id.b_status);

            title.setText(list.get(position).title);
            author.setText(list.get(position).author);
            edition.setText(list.get(position).edition);
            location.setText(list.get(position).location);

            int temp_Status = Integer.parseInt(list.get(position).status);
            if(temp_Status==1){
                status.setText("Available");
                status.setTextColor(getResources().getColor(R.color.positive));
            }else{
                status.setText("Not Available");
                status.setTextColor(getResources().getColor(R.color.negative));
            }
            return r;
        }
    }

    class SearchTask extends AsyncTask<String,Void,String> {

        Context context;

        public SearchTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String sign_url = MainActivity.IP+"library/get_books.php";


            try {
                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


                String data = URLEncoder.encode("title", "UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8")+"&"+
                        URLEncoder.encode("author","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line ="";

                while ((line = bufferedReader.readLine())!=null){
                    Log.i("Akhtar2", line);
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.i("Akhtar3", response + "");
                return SetUpList(response);


            } catch (Exception e) {

                e.printStackTrace();
            }
            return "e";
        }

        private String SetUpList(String data){
            Log.i("Akhtar3","Set up Book list");

            String[] columns = data.split("lol");


            if(columns.length>0) {
                bookClassArrayList.clear();
                for (String column : columns) {
                    Log.i("Akhtar2",column);
                    String[] row = column.split("feg");
                    for(String a : row){
                        Log.i("Akhtar2",a);
                    }
//public BookClass(String title,String author,String edition, String desc, String location,String status){
                   /* echo $b_title."feg";
                    echo $b_author."feg";
                    echo $b_edition."feg"
                    echo $b_description."feg";
                    echo $b_location."feg";
                    echo $b_status."lol";*/
                    bookClassArrayList.add(new BookClass(row[0],row[1],row[2],row[3],row[4],row[5]));


                }



                return "t";
            }else{

                return "f";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Akhtar", "Book Search task Finished");
            if (s.compareTo("t")==0) {
                listView.setAdapter(new SearchAdapter(bookClassArrayList));
                if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }else if (s.compareTo("f")==0){
                Toast.makeText(context,"No Books found",Toast.LENGTH_SHORT).show();
                if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }else{
                Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show();
            }


        }
    }
}
