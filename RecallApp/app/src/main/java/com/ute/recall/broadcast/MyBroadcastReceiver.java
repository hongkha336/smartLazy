package com.ute.recall.broadcast;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.ute.recall.global.globalBackground;
import com.ute.recall.global.modelGolbal;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static String THINGSPEAK_CHANNEL_ID ="1";
    private static String THINGSPEAK_READ_KEY;
    private static String THINGSPEAK_WRITE_KEY;
    /* Be sure to use the correct fields for your own app*/
    private static final String FIELD_TEMP = "field1";
    private static final String FIELD_HUMI = "field2";
    private static final String FIELD_SOIL= "field3";
    private static final String FIELD_PUMP = "field4";
    private static final String FIELD_READ_DATA = "field5";
    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?api_key=";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";
    private static final String THINGSPEAK_API_KEY_STRING = "api_key=";

    private static  String Soil = "";
    private static  String Temp = "null";
    private static  String Humi = "";
    private static  String isPump = "";

    Context activity;

    @Override
    public void onReceive(Context context, Intent intent) {

        activity = context;

        THINGSPEAK_CHANNEL_ID = intent.getStringExtra("THINGSPEAK_CHANNEL_ID");

        modelGolbal m = globalBackground.getChannelByID(THINGSPEAK_CHANNEL_ID);
        Temp = m.getTemp();
        Humi = m.getHumi();
        isPump = m.getIsPump();
        Soil = m.getSold();

        THINGSPEAK_WRITE_KEY = m.getWriteKey();
        THINGSPEAK_READ_KEY = m.getReadKey();
   //   isPump = intent.getStringExtra("pump");

        new SendSDataToThingSpeak().execute();


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.valueOf(THINGSPEAK_CHANNEL_ID));
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
            Toast.makeText(activity,"Pump action at "+THINGSPEAK_CHANNEL_ID ,Toast.LENGTH_LONG ).show();
        }
    }





}
