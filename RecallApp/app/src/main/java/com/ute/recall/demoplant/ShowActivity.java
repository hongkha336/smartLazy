package com.ute.recall.demoplant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ute.recall.R;
import com.ute.recall.global.userdata;
import com.ute.recall.services.BackgroundService;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowActivity extends AppCompatActivity {
    private static final String TAG = "HeThongNongNghiepTuoiCay";
    private static String THINGSPEAK_CHANNEL_ID;
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



    TextView txtHumidity, txtTemperature, txtSoilMoisture;
    ProgressBar progressBar_Temperature, progressBar_Humidity, progressBar_Soilmoisture_1;
    Switch switchWater;
    EditText edtTime;
    ImageButton imgbtnHistory, imgbtnConfig;
    TextView tvCountdown;
    SwipeRefreshLayout swipeRefreshLayout;
    private CountDownTimer countDownTimer;
    private static final long START_TIME = 0;
    private long timeLeftInMilliseconds;
    private long time = 0;
    private double isCheckPump = 0;
    private String isPump = "0";
    private double Temp, Humi, Soil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        addControls();
        addEvents();

    }

    private void addControls() {
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        txtTemperature = findViewById(R.id.txtTemperature);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtSoilMoisture = findViewById(R.id.txtSoilMoisture1);
        progressBar_Temperature = findViewById(R.id.progressBar_Temperature);
        progressBar_Humidity = findViewById(R.id.progressBar_Humidity);
        progressBar_Soilmoisture_1 = findViewById(R.id.progressBar_Soilmoisture_1);
        switchWater = findViewById(R.id.switchWater);
        edtTime = findViewById(R.id.edtTime);
        imgbtnHistory = findViewById(R.id.imgbtnHistory);
        tvCountdown = findViewById(R.id.tvCountdown);
        imgbtnConfig = findViewById(R.id.imgbtnConfig);



        edtTime.setText("0");

        Intent intent = getIntent();
//        THINGSPEAK_CHANNEL_ID = intent.getStringExtra("ChannelID");
//        THINGSPEAK_WRITE_KEY = intent.getStringExtra("WriteKey");
//        THINGSPEAK_READ_KEY = intent.getStringExtra("ReadKey");

        THINGSPEAK_CHANNEL_ID = String.valueOf(userdata.getChannelID());
        THINGSPEAK_WRITE_KEY = userdata.getWritekey();
        THINGSPEAK_READ_KEY = userdata.getReadkey();


        imgbtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, ConfigActivity.class);
                intent.putExtra("ChannelID", String.valueOf(userdata.getChannelID()));
                intent.putExtra("WriteKey", userdata.getWritekey());
                intent.putExtra("ReadKey", userdata.getReadkey());
                startActivity(intent);
            }
        });

    }

    private void addEvents() {
        try {
            new FetchThingspeakTask().execute();
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }
        switchWater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //new FetchThingspeakTask().execute();
                    double time2 = Double.parseDouble(edtTime.getText().toString());
                    time=(int) Math.round(time2);
                    if(time > 0 && time <= 1000 && isCheckPump==0 ){
                        isPump = edtTime.getText().toString();
                        new SendSDataToThingSpeak().execute();
                        edtTime.setEnabled(false);

                    }
                    else if(time < 15 || time > 1000 && isCheckPump==0 ) {
                        edtTime.setText("0");
                        switchWater.setChecked(false);
                    //    Toast.makeText(ShowActivity.this, "Time Syntax Error", Toast.LENGTH_LONG).show();
                    }
                    else if(time==0&&isCheckPump==0){
                        Toast.makeText(ShowActivity.this, "Fill Time Text", Toast.LENGTH_LONG).show();
                        switchWater.setChecked(false);
                    }

                    else if(isCheckPump!=0&&time!=0){
                        switchWater.setChecked(true);

                    }
                    else if(isCheckPump!=0&&time==0){
                        Toast.makeText(ShowActivity.this, "Loading Data. Please wait.", Toast.LENGTH_LONG).show();
                        switchWater.setChecked(false);
                    }
                }
                else {
                    //if(countDownTimer!=null) stopTime();
                    isPump = "0";
                    edtTime.setEnabled(true);
                    new SendSDataToThingSpeak().execute();
                    timeLeftInMilliseconds = START_TIME;
                    edtTime.setText("0");
                    tvCountdown.setText("");
                }
            }
        });
        imgbtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchThingspeakTask().execute();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });
    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
    private void startTime() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTime();
            }
            @Override
            public void onFinish() {
                switchWater.setChecked(false);
            }
        }.start();
    }

    private void stopTime(){
        countDownTimer.cancel();
    }

    private void updateTime() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        //String timeLeftFormatted = String .format(Locale.getDefault(),"%02d",seconds);
        String timeLeftFormatted = "";
        if (seconds < 10 ) timeLeftFormatted = "0";
        timeLeftFormatted += seconds;
        tvCountdown.setText(timeLeftFormatted);
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

    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {

        }
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING +
                        THINGSPEAK_READ_KEY);
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
            if(response == null) {
                return;
            }
            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                double v1 = channel.getDouble(FIELD_TEMP);
                double v2 = channel.getDouble(FIELD_HUMI);
                double v3 = channel.getDouble(FIELD_SOIL);
                double v4 = channel.getDouble(FIELD_PUMP);
                isCheckPump = channel.getDouble(FIELD_PUMP);

                Humi=v2;
                Temp=v1;
                Soil=v3;

                txtHumidity.setText(v2+"");
                txtTemperature.setText(v1+"");
                txtSoilMoisture.setText(v3+"");

                progressBar_Temperature.setProgress((int)Math.round(v1));
                progressBar_Humidity.setProgress((int)Math.round(v2));
                progressBar_Soilmoisture_1.setProgress((int)Math.round(v3));
                if(v4!=0){
                    switchWater.setChecked(true);
                    edtTime.setText(v4+"");
                    edtTime.setEnabled(false);
                }
                else {
                    switchWater.setChecked(false);
                    edtTime.setText("0");
                    edtTime.setEnabled(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
