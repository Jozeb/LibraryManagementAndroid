package com.example.jazibhassan.thelibrary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


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

/**
 * Created by Jazib Hassan on 22-Nov-15.
 */
public class NotificationService extends Service {

    DbSource database;
    SharedPreferences sharedPreferences;

    public static final int MY_NOTIFICATION_ID = 1;
    public static long[] mVibratePattern = {200, 200,200};
    public static Uri ringtoneUri = RingtoneManager
            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            database = MainActivity.database;
            if (database == null) {
                database = new DbSource(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sharedPreferences = getSharedPreferences(MainActivity.PREF_USER, MODE_PRIVATE);
        final String userid = sharedPreferences.getString(MainActivity.USER_ID, "N/A");


        if (!(userid.compareTo("N/A") == 0)) {
            new SearchTask().execute(userid);
        }

        Log.i("Akhtar","repeat");
        Intent intent2 = new Intent(this, NotificationService.class);
        PendingIntent sender = PendingIntent.getService(this, 0, intent2, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, 5000, pi);
        return START_NOT_STICKY;
    }


    class SearchTask extends AsyncTask<String, Void, String> {


        public SearchTask() {

        }

        @Override
        protected String doInBackground(String... params) {
            String sign_url = MainActivity.IP + "library/get_notification.php";


            try {
                URL url = new URL(sign_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


                String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

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

        private String SetUpList(String data) {
            Log.i("Akhtar3", "Set up Noti list");

            String[] columns = data.split("lol");


            if (columns.length > 0) {

                ArrayList<NotificationClass> list = new ArrayList<NotificationClass>();

                for (String column : columns) {

                    String[] row = column.split("feg");

                    if(row.length==3){
                        list.add(new NotificationClass(row[0], row[1], row[2]));
                    }
                }
                if(list.size()>0){
                    database.put_noti_data(list);
                    NotificationClass obj = list.get(list.size() - 1);
                    setUpNotification(NotificationService.this, obj.message);
                }


                return "t";
            } else {

                return "f";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Akhtar", "Noti Search task Finished");


        }

    }

    public void setUpNotification(Context context, String tomessage) {

        Intent noti_intent = new Intent(context, NotificationActivity.class);
        PendingIntent noti_pend = PendingIntent.getActivity(context, MY_NOTIFICATION_ID, noti_intent, PendingIntent.FLAG_CANCEL_CURRENT);


        String title = "Library";


        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {

            NotificationCompat.Builder mBuilder;
            mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setSmallIcon(android.R.drawable.stat_notify_voicemail)
                    .setTicker(tomessage)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title)
                    .setSound(ringtoneUri)
                    .setContentText(tomessage)
                    .setAutoCancel(true);

            mBuilder.setVibrate(mVibratePattern);


            Intent resultIntent = new Intent(context, NotificationActivity.class);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(MY_NOTIFICATION_ID, mBuilder.build());
        } else {
            //Message Notifcation for HONEYCOMB ++
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
            /*Notification.Builder notificationBuilder = new Notification.Builder(context);*/
            notificationBuilder.setTicker(tomessage);


            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setContentIntent(noti_pend);
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(tomessage);
            notificationBuilder.setSound(ringtoneUri);

            notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_voicemail);
            notificationBuilder.setVibrate(mVibratePattern);



            Notification notification = notificationBuilder.getNotification();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(MY_NOTIFICATION_ID, notification);


        }
    }

}
