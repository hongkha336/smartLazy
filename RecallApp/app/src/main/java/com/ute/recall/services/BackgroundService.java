package com.ute.recall.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.ute.recall.Database.MyDatabaseHelper;
import com.ute.recall.R;
import com.ute.recall.broadcast.MyBroadcastReceiver;
import com.ute.recall.model.channelinfor;
import com.ute.recall.model.channelConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ute.recall.global.modelGolbal;
import com.ute.recall.global.globalBackground;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    private static String THINGSPEAK_CHANNEL_ID;
    private static String THINGSPEAK_READ_KEY;
    private static String THINGSPEAK_WRITE_KEY;
    public static Runnable runnable = null;
    private static final String FIELD_TEMP = "field1";
    private static final String FIELD_HUMI = "field2";
    private static final String FIELD_SOIL= "field3";
    private static final String FIELD_PUMP = "field4";
    private static final String FIELD_READ_DATA = "field5";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?api_key=";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    private static final String THINGSPEAK_API_KEY_STRING = "api_key=";
    private static final long START_TIME = 0;
    private long timeLeftInMilliseconds;
    private long time = 0;
    private double isCheckPump = 0;
    private String isPump = "0";
    private double Temp, Humi, Soil;
    private static List<channelinfor> listInfor;
    List<channelConfig> list;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private channelConfig getChannelConfig(int ChannelID)
    {
        for(channelConfig c : list)
        {
            if(c.getChannelID() == ChannelID)
                return  c;
        }
        return  null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                MyDatabaseHelper  myDatabaseHelper = new MyDatabaseHelper(context);
               list = myDatabaseHelper.getAllConfigs();
               listInfor = myDatabaseHelper.getAllUsers();


               new FetchThingspeakTask().execute();


                handler.postDelayed(runnable, 60000);
            }
        };

        handler.postDelayed(runnable, 60000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
     //  Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
      //  Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }



    class SendSDataToThingSpeak extends AsyncTask<Void, Void, String>{
        protected void onPreExecute() {

        }
        protected String doInBackground(Void... urls) {
            try {
                String pump = String.valueOf(isPump);
                //https://api.thingspeak.com/update?api_key=WPTGRCP59SRA7YBC&field4=0.0&field1=25.0&field2=50.0&field3=40.0&field5=2
                URL url = new URL(THINGSPEAK_UPDATE_URL + THINGSPEAK_WRITE_KEY + "&"+FIELD_TEMP+"="+Temp+"&"+FIELD_HUMI+"="+Humi+
                        "&"+FIELD_SOIL+"="+Soil+ "&"+FIELD_PUMP+"="+pump);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {

        }
    }


    class FetchThingspeakTask extends AsyncTask<Void, Void, List<String>> {

        protected void onPreExecute() {

        }
        protected List<String> doInBackground(Void... urls) {
            List<String> myRespone = new ArrayList<>();
            for(channelinfor c: listInfor) {
                try {
                    URL url = new URL(THINGSPEAK_CHANNEL_URL + String.valueOf(c.getChannelID()) +
                            THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING +
                            c.getReadkey());

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        myRespone.add( stringBuilder.toString());
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);

                }
            }

            if(myRespone.size() == 0)
                return null;
            return myRespone;
        }
        protected void onPostExecute(List<String> responses) {



            if(responses == null) {
                return;
            }

            int index = 0;
            for(String response : responses) {
                try {
                    JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                    double v1 = channel.getDouble(FIELD_TEMP);
                    double v2 = channel.getDouble(FIELD_HUMI);
                    double v3 = channel.getDouble(FIELD_SOIL);
                    double v4 = channel.getDouble(FIELD_PUMP);
                    isCheckPump = channel.getDouble(FIELD_PUMP);

                    Humi = v2;
                    Temp = v1;
                    Soil = v3;


                    isPump = "10";

                    String content = "Soil :" + Soil + " - Temp: " + Temp + " - Humi:  " + Humi;

                    THINGSPEAK_CHANNEL_ID = String.valueOf(listInfor.get(index).getChannelID());
                    THINGSPEAK_READ_KEY = String.valueOf(listInfor.get(index).getReadkey());
                    THINGSPEAK_WRITE_KEY = String.valueOf(listInfor.get(index).getWritekey());
                    channelConfig c = getChannelConfig(listInfor.get(index).getChannelID());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String md = " at " + formatter.format(date);

                    channelConfig channelConfig = new channelConfig();
                    MyDatabaseHelper mdb = new MyDatabaseHelper(BackgroundService.this);
                    channelConfig = mdb.getConfig(Integer.valueOf(THINGSPEAK_CHANNEL_ID));

                    if (Double.valueOf(Humi) <= Double.valueOf(channelConfig.getHumi())

                            &&
                            Double.valueOf(Soil) <= Double.valueOf(channelConfig.getSoil())

                            &&
                            Double.valueOf(Temp) >= Double.valueOf(channelConfig.getTemp())


                    )
                    {

                        String channelName = listInfor.get(index).getChannelName();
                        try {
                            if (channelName.equals("null"))
                                channelName = THINGSPEAK_CHANNEL_ID;
                        } catch (Exception e) {
                            channelName = THINGSPEAK_CHANNEL_ID;
                        }

                        isPump = String.valueOf(channelConfig.getPump());


                        if(channelConfig.getAuto() == 0) {
                            Intent snoozeIntent = new Intent(context, MyBroadcastReceiver.class);
                            snoozeIntent.putExtra("THINGSPEAK_CHANNEL_ID", THINGSPEAK_CHANNEL_ID);

                            //snoozeIntent.putExtra("pump", THINGSPEAK_CHANNEL_ID);
                            snoozeIntent.setAction("android.intent.action.NOTI");

                            globalBackground.addModel(new modelGolbal(THINGSPEAK_CHANNEL_ID, THINGSPEAK_READ_KEY, THINGSPEAK_WRITE_KEY,
                                    String.valueOf(Temp), String.valueOf(Humi), String.valueOf(Soil), isPump));


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                                PendingIntent snoozePendingIntent =
                                        PendingIntent.getBroadcast(context, Integer.valueOf(THINGSPEAK_CHANNEL_ID), snoozeIntent, 0);

                                CharSequence name = getString(R.string.channel_name);
                                String description = THINGSPEAK_CHANNEL_ID.toString();
                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                NotificationChannel mchannel = new NotificationChannel(THINGSPEAK_CHANNEL_ID, name, importance);
                                mchannel.setDescription(description);
                                mchannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(mchannel);


                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, THINGSPEAK_CHANNEL_ID.toString())
                                        .setSmallIcon(R.drawable.wt)
                                        .setContentTitle( channelName + " " +md)
                                        .setContentText(content)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(snoozePendingIntent)
                                        .addAction(0, getString(R.string.pump),
                                                snoozePendingIntent)
                                        .setAutoCancel(true);


                                NotificationManagerCompat mnotificationManager = NotificationManagerCompat.from(context);
                                int notificationId = Integer.valueOf(THINGSPEAK_CHANNEL_ID);
                                if(channelConfig.getWarn() == 1)
                                    mnotificationManager.notify(notificationId, mBuilder.build());
                            } else {


                                PendingIntent snoozePendingIntent =
                                        PendingIntent.getBroadcast(context, Integer.valueOf(THINGSPEAK_CHANNEL_ID), snoozeIntent, 0);


                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1222")
                                        .setSmallIcon(R.drawable.wt)
                                        .setContentTitle(channelName + " " +md)
                                        .setContentText(content)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        //       .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(snoozePendingIntent)
                                        .addAction(0, getString(R.string.pump),
                                                snoozePendingIntent)
                                        .setAutoCancel(true);


                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                if(channelConfig.getWarn() == 1)
                                notificationManager.notify(Integer.valueOf(THINGSPEAK_CHANNEL_ID), mBuilder.build());
                            }
                        }
                        else
                        {
                            Intent snoozeIntent = new Intent(context, MyBroadcastReceiver.class);

                            new SendSDataToThingSpeak().execute();
                            //snoozeIntent.putExtra("pump", THINGSPEAK_CHANNEL_ID);


                            Toast.makeText(BackgroundService.this,"Auto pump start",Toast.LENGTH_SHORT).show();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                                PendingIntent snoozePendingIntent =
                                        PendingIntent.getBroadcast(context, Integer.valueOf(THINGSPEAK_CHANNEL_ID), snoozeIntent, 0);

                                CharSequence name = getString(R.string.channel_name);
                                String description = THINGSPEAK_CHANNEL_ID.toString();
                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                NotificationChannel mchannel = new NotificationChannel(THINGSPEAK_CHANNEL_ID, name, importance);
                                mchannel.setDescription(description);
                                mchannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                notificationManager.createNotificationChannel(mchannel);


                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, THINGSPEAK_CHANNEL_ID.toString())
                                        .setSmallIcon(R.drawable.wt)
                                        .setContentTitle( channelName + " AUTO PUMP " +md )
                                        .setContentText(content)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(snoozePendingIntent)
                                        .setAutoCancel(true);


                                NotificationManagerCompat mnotificationManager = NotificationManagerCompat.from(context);
                                int notificationId = Integer.valueOf(THINGSPEAK_CHANNEL_ID);
                                if(channelConfig.getWarn() == 1)
                                mnotificationManager.notify(notificationId, mBuilder.build());
                            } else {


                                PendingIntent snoozePendingIntent =
                                        PendingIntent.getBroadcast(context, Integer.valueOf(THINGSPEAK_CHANNEL_ID), snoozeIntent, 0);



                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1222")
                                        .setSmallIcon(R.drawable.wt)
                                        .setContentTitle(channelName + " AUTO PUMP " + md)
                                        .setContentText(content)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                        //       .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(snoozePendingIntent)

                                        .setAutoCancel(true);


                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                if(channelConfig.getWarn() == 1)
                                notificationManager.notify(Integer.valueOf(THINGSPEAK_CHANNEL_ID), mBuilder.build());
                            }

                        }
                    }
                    } catch(JSONException fe){
                        fe.printStackTrace();
                    }
                    index++;

            }
        }
    }
}