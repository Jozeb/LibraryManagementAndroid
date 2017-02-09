package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jazibhassan.thelibrary.dbpackage.DbSource;

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

public class IssueActivity extends Activity {

    ListView listView;
    ArrayList<HistoryClass> historyClassArrayList;
    DbSource database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        database = MainActivity.database;

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREF_USER, MODE_PRIVATE);
        String userid = sharedPreferences.getString(MainActivity.USER_ID,"N/A");

        listView  = (ListView) findViewById(R.id.hist_list);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        historyClassArrayList = database.get_issue_data();
        listView.setAdapter(new HistoryAdapter(historyClassArrayList));
        if(mWifi.isConnected()){
            historyClassArrayList = new ArrayList<HistoryClass>();
            new HistoryTask(this).execute(userid);
        }

    }

    class HistoryAdapter extends BaseAdapter {

        ArrayList<HistoryClass> list;

        public HistoryAdapter(ArrayList<HistoryClass> historyClassArrayList){
            this.list = historyClassArrayList;

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
                r =  getLayoutInflater().inflate(R.layout.historyrow,null);
            }else{
                r= view;
            }

            TextView title = (TextView) r.findViewById(R.id.history_title);
            TextView author = (TextView) r.findViewById(R.id.history_author);
            TextView edition = (TextView) r.findViewById(R.id.history_date);
            TextView issue_date = (TextView) r.findViewById(R.id.issue_date);
            TextView due_date = (TextView) r.findViewById(R.id.return_date);
            TextView due_t = (TextView) r.findViewById(R.id.due_Date);


            title.setText(list.get(position).title);
            author.setText(list.get(position).author);
            edition.setText(list.get(position).edition);
            issue_date.setText(list.get(position).issuedate);
            due_date.setText(list.get(position).duedate);
            due_t.setText("Due Date");
            return r;
        }
    }

    class HistoryTask extends AsyncTask<String,Void,Boolean> {

        Context context;

        public HistoryTask(Context context){
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String sign_url = MainActivity.IP+"library/get_issue.php";


            try {
                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


                String data = URLEncoder.encode("userid", "UTF-8")+"="+URLEncoder.encode(params[0],"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line ="";

                while ((line = bufferedReader.readLine())!=null){
                    Log.i("Akhtar2 buffer: ", line);
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
            return false;
        }

        private boolean SetUpList(String data){
            Log.i("Akhtar3","Set up History list");

            String[] columns = data.split("lol");


            if(columns.length>0) {
                historyClassArrayList.clear();
                for (String column : columns) {
                    Log.i("Akhtar2 final",column);
                    String[] row = column.split("feg");
                    if(row.length==6){
                        historyClassArrayList.add(new HistoryClass(row[0],row[1],row[2],row[3],row[4],row[5]));
                    }else{
                        historyClassArrayList.add(new HistoryClass(row[0],row[1],row[2],row[3],"Null",row[4]));
                    }


                }



                return true;
            }else{

                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            Log.i("Akhtar", "Issue Activity task Finished");
            if (s) {
                database.delete_issue_data();
                database.put_issue_data(historyClassArrayList);
                listView.setAdapter(new HistoryAdapter(historyClassArrayList));
            }else{
                Toast.makeText(context, "Issue Books First", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
