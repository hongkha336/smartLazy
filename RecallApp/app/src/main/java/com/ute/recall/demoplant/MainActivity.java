package com.ute.recall.demoplant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.ute.recall.Database.MyDatabaseHelper;

import com.ute.recall.R;
import com.ute.recall.constant.constString;
import com.ute.recall.datacontroller.HttpHandler;
import com.ute.recall.global.userdata;
import com.ute.recall.model.channelinfor;
import com.ute.recall.model.channelConfig;
import com.ute.recall.services.BackgroundService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    EditText edtChannel, edtRead, edtWrite;
    Button btnLogin, btnQR;
    MyDatabaseHelper databaseHelper;

    //CheckBox chkSaveInformation;
    String namePreferences = "thongtindangnhap";
    TextView lbSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();



        lbSwitch = findViewById(R.id.lbSwitch);
        lbSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SwitchActivity.class);
                startActivity(intent);
            }
        });
        databaseHelper = new MyDatabaseHelper(MainActivity.this);
        List<channelinfor> channelinfors = databaseHelper.getAllUsers();
        userdata.ml = new ArrayList<>();
        for (channelinfor c : channelinfors)
            userdata.ml.add(c);


        btnQR = findViewById(R.id.btnQR);

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QRActivity.class);
                startActivityForResult(intent, 321);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 321) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                String str =data.getStringExtra("DATA");

                String cnID = "";
                String read = "";
                String write ="";
            //    Toast.makeText(MainActivity.this,str.toString(),Toast.LENGTH_SHORT).show();

                try {
                    while (str.length() > 0 && str.charAt(0) != ' ') {
                        cnID = cnID + str.charAt(0);
                        str = str.substring(1, str.length() );
                    }

                    if (str.length() > 0 && str.charAt(0) == ' ')
                        str = str.substring(1, str.length() );

                    while (str.length() > 0 && str.charAt(0) != ' ') {
                        read = read + str.charAt(0);
                        str = str.substring(1, str.length() );
                    }

                    if (str.length() > 0 && str.charAt(0) == ' ')
                        str = str.substring(1, str.length());

                    write = str;


                    if(!cnID.equals("") && !read.equals("") && !write.equals(""))
                    {

                        userdata.setChannelID(Integer.valueOf(cnID));
                        userdata.setWritekey(String.valueOf(write));
                        userdata.setReadkey(String.valueOf(read));
                        userdata.setChannelName(cnID);
                        new GetData().execute();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "QR Wrong Format", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "LQR Wrong Format", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }





    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangNhap();
            }
        });

    }

    private void xuLyDangNhap() {
//        if(edtRead.getText().toString().equals("admin") && edtWrite.getText().toString().equals("123")){
//            Intent intent = new Intent(MainActivity.this, ShowActivity.class);
//            startActivity(intent);
//        }



        if (edtChannel.getText().toString().isEmpty() == false &&
                edtWrite.getText().toString().isEmpty() == false &&
                edtWrite.getText().toString().isEmpty() == false) {

            userdata.setChannelID(Integer.valueOf(edtChannel.getText().toString()));
            userdata.setWritekey(String.valueOf(edtWrite.getText().toString()));
            userdata.setReadkey(String.valueOf(edtRead.getText().toString()));
            userdata.setChannelName(String.valueOf(edtChannel.getText().toString()));
            new GetData().execute();





        }
        else Toast.makeText(MainActivity.this,"Vui lòng điền đầy đủ thông tin!",Toast.LENGTH_LONG).show();
    }

    private  boolean isRightServer(int channel, String readKey, String writeKey)
    {
        return true;
    }

    private void addControls() {
        edtChannel = findViewById(R.id.edtChannel);
        edtRead = findViewById(R.id.edtRead);
        edtWrite = findViewById(R.id.edtWrite);
        btnLogin = findViewById(R.id.btnLogin);
       // btnExit = findViewById(R.id.btnExit);
        //chkSaveInformation = findViewById(R.id.chkSaveInformation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(namePreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("channelID", edtChannel.getText().toString());
        editor.putString("read", edtRead.getText().toString());
        editor.putString("write", edtWrite.getText().toString());
        //editor.putBoolean("save",chkSaveInformation.isChecked());
        editor.commit();
        startService(new Intent(this, BackgroundService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
//
        try{
            this.stopService(new Intent(MainActivity.this,
                    BackgroundService.class));
        }
        catch (Exception ee)
        {}

        SharedPreferences preferences = getSharedPreferences(namePreferences, MODE_PRIVATE);
        String channel = preferences.getString("channelID", "");
        String read = preferences.getString("read", "");
        String write = preferences.getString("write", "");
        //Boolean save = preferences.getBoolean("save",true);
        // if (save){
        edtChannel.setText(channel);
        edtWrite.setText(write);
        edtRead.setText(read);


    }


    private String TAG = MainActivity.class.getSimpleName();
    public class GetData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Login, please wait",Toast.LENGTH_SHORT).show();
            btnLogin.setEnabled( false);


        }

        @Override
        protected String doInBackground(Void... arg0) {

           try {
               HttpHandler sh = new HttpHandler();
               // Making a request to url and getting response
               String url = "https://api.thingspeak.com/channels/" + String.valueOf(userdata.getChannelID()) + "/feeds.json?api_key=" + userdata.getWritekey();
               String jsonStr = sh.makeServiceCall(url);
               String url2 = "https://api.thingspeak.com/channels/" + String.valueOf(userdata.getChannelID()) + "/feeds.json?api_key=" + userdata.getReadkey();
               String jsonStr2 = sh.makeServiceCall(url2);



            if(jsonStr.equals(constString.CODE_404))
            {
                return  constString.CODE_404;
            }

            if(jsonStr.equals(constString.CODE_404))
            {
                return  constString.NULL_VALUE;
            }

            if(jsonStr2.equals(constString.CODE_404))
            {
                return  constString.NULL_VALUE;
            }
            return constString.SUCCESS_VALUE;

           }catch (Exception e)
           {
               return  constString.CODE_404;
           }


        }

        private boolean isExist(channelinfor ch)
        {
            for(channelinfor c: userdata.ml)
            {
                if(c.getChannelID() == ch.getChannelID()) {


                        userdata.setChannelName(c.getChannelName());
                    try{
                       channelConfig mc = databaseHelper.getConfig(ch.getChannelID());
                       if(mc == null )
                       {
                           userdata.createChannelConfigDefault();
                           channelConfig cc = userdata.getChannelConfig();
                           databaseHelper.addConfig(cc);

                       }
                       else
                       {
                           userdata.setChannelConfig(mc);
                       }
                    }
                    catch (Exception e)
                    {
                        userdata.createChannelConfigDefault();
                    }


                    return true;
                }
            }
            userdata.createChannelConfigDefault();
            return false;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Toast.makeText(MainActivity.this,"Post "+result,Toast.LENGTH_LONG).show();
            btnLogin.setEnabled( true);


            String str = result.toString();

            if(str.equals("Post 1") || str.equals("1") )
            {
                channelinfor ch = new channelinfor(Integer.valueOf(userdata.getChannelID()),userdata.getWritekey(),userdata.getReadkey(),
                        String.valueOf(userdata.getChannelID()));
                if(!isExist(ch)) {

                    userdata.ml.add(ch);
                    databaseHelper.addUser(ch);
                    databaseHelper.addConfig(userdata.getChannelConfig());
                }

                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                intent.putExtra("ChannelID", String.valueOf(userdata.getChannelID()));
                intent.putExtra("WriteKey", userdata.getWritekey());
                intent.putExtra("ReadKey",userdata.getReadkey());
                startActivity(intent);
            }
            else
            {
                if(str.equals("-1")) {
                    Toast.makeText(MainActivity.this, "Wrong read key or write key, try again", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Wrong channel id, try again",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
