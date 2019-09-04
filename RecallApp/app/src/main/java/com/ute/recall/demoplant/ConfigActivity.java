package com.ute.recall.demoplant;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.ute.recall.Database.MyDatabaseHelper;
import com.ute.recall.global.userdata;
import com.ute.recall.model.channelConfig;
import com.ute.recall.R;
import com.ute.recall.model.channelinfor;

public class ConfigActivity extends AppCompatActivity {


    Button btnSave;
    EditText edtTemp, edtHumi, edtSoil, edtChannelName, edtPump;
    Switch swAuto, swWarn;
    MyDatabaseHelper databaseHelper;
    TextView lbID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        databaseHelper = new MyDatabaseHelper(ConfigActivity.this);
        swWarn = findViewById(R.id.sw_warn);
        swAuto= findViewById(R.id.sw_auto);
        edtTemp = findViewById(R.id.edttemp);
        edtHumi = findViewById(R.id.edtHumi);
        edtSoil = findViewById(R.id.edtSoil);
        btnSave = findViewById(R.id.btnSave);
        edtPump = findViewById(R.id.edtPump);
        edtChannelName = findViewById(R.id.edtChannelName);
        lbID =findViewById(R.id.lbIdC);
        lbID.setText(String.valueOf(userdata.getChannelID()));


        edtPump.setText(String.valueOf(userdata.getChannelConfig().getPump()));
        edtChannelName.setText(String.valueOf(userdata.getChannelName()));
        edtHumi.setText(String.valueOf(userdata.getChannelConfig().getHumi()));
        edtTemp.setText(String.valueOf(userdata.getChannelConfig().getTemp()));
        edtSoil.setText(String.valueOf(userdata.getChannelConfig().getSoil()));

        if(userdata.getChannelConfig().getWarn() == 0)
        {
            swWarn.setChecked(false);
        }
        else
        {
            swWarn.setChecked(true);
        }


        if(userdata.getChannelConfig().getAuto() == 0)
        {
            swAuto.setChecked(false);
        }
        else
        {
            swAuto.setChecked(true);
        }




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(ConfigActivity.this);
                builder1.setMessage("Save configurations?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                channelConfig c = userdata.getChannelConfig();
                                c.setSoil(Double.valueOf(edtSoil.getText().toString()));
                                c.setHumi(Double.valueOf(edtHumi.getText().toString()));
                                c.setTemp(Double.valueOf(edtTemp.getText().toString()));
                                c.setPump(Double.valueOf(edtPump.getText().toString()));
                                if(swAuto.isChecked())
                                    c.setAuto(1);
                                else c.setAuto(0);

                                if(swWarn.isChecked())
                                    c.setWarn(1);
                                else c.setWarn(0);


                                userdata.setChannelConfig(c);

                                channelinfor ci = new channelinfor();
                                ci.setChannelName(edtChannelName.getText().toString());
                                ci.setChannelID(userdata.getChannelID());
                                ci.setReadkey(userdata.getReadkey());
                                ci.setWritekey(userdata.getWritekey());

                                userdata.setChannelName(edtChannelName.getText().toString());

                                databaseHelper.updateUser(ci);
                                databaseHelper.updateConfig(c);
                                Toast.makeText(ConfigActivity.this,"Configurations were saved", Toast.LENGTH_SHORT).show();

                                dialog.cancel();
                                try {
                                    Thread.sleep(500);
                                }catch (Exception e)
                                {}
                                finish();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });

    }
}
