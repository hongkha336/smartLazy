package com.ute.recall.demoplant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ute.recall.Database.MyDatabaseHelper;
import com.ute.recall.global.userdata;
import com.ute.recall.model.channelinfor;

import com.ute.recall.R;
import com.ute.recall.adapter.ChannelAdapter;
import com.ute.recall.model.channelConfig;
import java.util.ArrayList;
import java.util.List;

public class SwitchActivity extends AppCompatActivity {

    ListView lv;
    TextView lbAdd;
    MyDatabaseHelper  databaseHelper;
    public int itm = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        lv = findViewById(R.id.lvChannel);
        databaseHelper = new MyDatabaseHelper(SwitchActivity.this);
        List<channelinfor> channelinfors = databaseHelper.getAllUsers();
        userdata.ml = new ArrayList<>();
        for(channelinfor c : channelinfors)
            userdata.ml.add(c);

        List<channelinfor> lvs = userdata.ml;
        ChannelAdapter adapter = new ChannelAdapter(SwitchActivity.this,R.layout.channel,lvs);
        lv.setAdapter(adapter);

        lbAdd = findViewById(R.id.lbAdd);




        lbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwitchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


//        lv.setLongClickable(true);
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                return false;
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itm = position;
                String name = "null";
                try {
                    name = userdata.ml.get(itm).getChannelName();
                    if (name.equals("null"))
                        name = String.valueOf(userdata.ml.get(itm).getChannelID());
                }catch (Exception e)
                {
                    name = String.valueOf(userdata.ml.get(itm).getChannelID());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SwitchActivity.this);
                builder.setTitle("SmartLazy");
                builder.setMessage("Choose Action for " +name+"?");
                builder.setCancelable(false);
                builder.setNeutralButton("Foget", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        databaseHelper.deleteNoteByID(String.valueOf(userdata.ml.get(itm).getChannelID()));
                        //       userdata.ml.
                        List<channelinfor> channelinfors = databaseHelper.getAllUsers();
                        userdata.ml = new ArrayList<>();
                        for(channelinfor c : channelinfors)
                            userdata.ml.add(c);
                        List<channelinfor> lvs = userdata.ml;
                        ChannelAdapter adapter = new ChannelAdapter(SwitchActivity.this,R.layout.channel,lvs);
                        lv.setAdapter(adapter);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(SwitchActivity.this,"Login,Please wait...",Toast.LENGTH_SHORT).show();
                        userdata.setReadkey(userdata.ml.get(itm).getReadkey());
                        userdata.setWritekey(userdata.ml.get(itm).getWritekey());
                        userdata.setChannelID(userdata.ml.get(itm).getChannelID());
                        userdata.setChannelName(userdata.ml.get(itm).getChannelName());
                        channelConfig c = databaseHelper.getConfig(userdata.ml.get(itm).getChannelID());
                        if(c == null)
                        {
                            userdata.createChannelConfigDefault();
                            channelConfig cc = userdata.getChannelConfig();
                            databaseHelper.addConfig(cc);
                        }
                        else {
                            userdata.setChannelConfig(c);
                        }

                        Intent intent = new Intent(SwitchActivity.this, ShowActivity.class);
                        intent.putExtra("ChannelID", String.valueOf(userdata.getChannelID()));
                        intent.putExtra("WriteKey", userdata.getWritekey());
                        intent.putExtra("ReadKey", userdata.getReadkey());
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}
