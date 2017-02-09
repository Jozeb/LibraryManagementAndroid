package com.example.jazibhassan.thelibrary;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jazibhassan.thelibrary.dbpackage.DbSource;

import java.util.ArrayList;

public class NotificationActivity extends Activity {

    DbSource database;
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        database = MainActivity.database;
        if(database==null){
            database = new DbSource(this);
        }

        listview = (ListView) findViewById(R.id.noti_list);
        listview.setAdapter(new NotificationAdapter());

        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete_noti_data();
                listview.setAdapter(new NotificationAdapter());
            }
        });


    }

    class NotificationAdapter extends BaseAdapter{

        ArrayList<NotificationClass> list;
        Typeface typeface;
        public NotificationAdapter(){
            list = database.get_noti_data();
            typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            View r;

            if(view == null){
                r =  getLayoutInflater().inflate(R.layout.notification_row,null);
            }else{
                r= view;
            }

            TextView date = (TextView) r.findViewById(R.id.noti_date);
            TextView msg = (TextView) r.findViewById(R.id.noti_mesg);

            date.setTypeface(typeface);
            msg.setTypeface(typeface);
            date.setText(list.get(position).date);
            msg.setText(list.get(position).message);
            return r;
        }
    }


}
