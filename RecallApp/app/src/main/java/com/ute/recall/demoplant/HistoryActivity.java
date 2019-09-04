package com.ute.recall.demoplant;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.ute.recall.R;
import com.ute.recall.adapter.DateLayout;
import com.ute.recall.adapter.HistoryAdapter;
import com.ute.recall.adapter.LogAdapter;
import com.ute.recall.datacontroller.HttpHandler;
import com.ute.recall.global.userdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ute.recall.model.feeds;


public class HistoryActivity extends AppCompatActivity {

    public HistoryAdapter adapter;
    ListView lvDetails;
    List<feeds> feedsList;
    List<List<feeds>> feedSep;

    private String getDate(String d)
    {
        String str = "";
        while(d.length() > 0 && d.charAt(0) != 'T')
        {
            str = str + String.valueOf(d.charAt(0));
            d = d.substring(1,str.length()-1);
        }
        return str;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        feedsList = new ArrayList<feeds>();
        feedSep = new ArrayList<>();
        addControls();
        new GetFeeds().execute();
    }

    private void addControls() {
        lvDetails = findViewById(R.id.lvDetails);
    }


    private class GetFeeds extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(HistoryActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
        }



        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String url = "https://api.thingspeak.com/channels/" + String.valueOf(userdata.getChannelID()) + "/feeds.json?api_key=" + userdata.getWritekey() +"&timezone=Asia/Bangkok";
                String jsonStr = sh.makeServiceCall(url);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray mfeeds = jsonObj.getJSONArray("feeds");


                        List<feeds> mf = new ArrayList<>();


                        // looping through All Contacts
                        for (int i = 0; i < mfeeds.length(); i++) {
                            JSONObject c = mfeeds.getJSONObject(i);
                            String field1 = c.getString("field1");
                            String field2 = c.getString("field2");
                            String field3 = c.getString("field3");
                            String field4 = c.getString("field4");
                            String created_at = c.getString("created_at");
                            feeds f = new feeds(created_at,0,field1,field2,field3,field4);
//
//                            if(mf.size() > 0 && getDate(created_at).equals(getDate(mf.get(0).getCreated_at())) )
//                            {
//                                mf.add(f);
//                            }
//                            else
//                            {
//                                feedSep.add(mf);
//                                mf = new ArrayList<>();
//                                mf.add(f);
//                            }



                            // adding contact to contact list
                            feedsList.add(f);
                        }

                        feedSep.add(mf);
                    } catch (final JSONException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }catch (Exception e)
            {}

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Collections.reverse(feedsList);
             // LogAdapter adapter = new LogAdapter(HistoryActivity.this,R.layout.logitem, feedsList);
            HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this,R.layout.item,feedsList);
            lvDetails.setAdapter(adapter);
        }
    }


}
