package com.ute.recall.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ute.recall.R;
import com.ute.recall.model.History;
import com.ute.recall.model.feeds;

import org.w3c.dom.Text;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    Activity context;
    int resource;
    List<feeds> feedsList;

    public HistoryAdapter(Activity context,int mLayout,  List<feeds> feedsList) {

        this.context = context;
        this.resource = mLayout;
        this.feedsList = feedsList;
    }

    @Override
    public int getCount() {
        return feedsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public  View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = this.context.getLayoutInflater();
//        View customView = inflater.inflate(this.resource, null);
//        TextView txtHis_Time = customView.findViewById(R.id.txtHis_Time);
//        TextView txtHis_Temp = customView.findViewById(R.id.txtHis_Temp);
//        TextView txtHis_Hum = customView.findViewById(R.id.txtHis_Hum);
//        TextView txtHis_Soil = customView.findViewById(R.id.txtHis_Soil);
//
//        History history = getItem(position);
//        txtHis_Time.setText(history.getTime());
//        txtHis_Temp.setText(history.getTemperature());
//        txtHis_Hum.setText(history.getHumidity());
//        txtHis_Soil.setText(history.getSoil());


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource,null);
        TextView txtHis_Date = convertView.findViewById(R.id.txtHis_Time);
        TextView txtHis_Time = convertView.findViewById(R.id.txtHis_Time2);
        TextView txtHis_Temp = convertView.findViewById(R.id.txtHis_Temp);
        TextView txtHis_Hum = convertView.findViewById(R.id.txtHis_Hum);
        TextView txtHis_Soil = convertView.findViewById(R.id.txtHis_Soil);
        TextView txtPump = convertView.findViewById(R.id.txtHis_Pump);

      feeds history = feedsList.get(position);
        txtPump.setText("Pump: " +history.getField4() + " s");

        txtHis_Hum.setText("Humi: "+history.getField2() +" %");
        txtHis_Soil.setText("Soil: " +history.getField3() +" %");


        String temp = history.getField1();

        try{
            temp = temp.substring(0,4);
        }catch (Exception e)
        {

        }
        txtHis_Temp.setText("Temp: "+temp +" °C");

        String str = history.getCreated_at();
        String d = "";
        while(str.length() >0 &&str.charAt(0) != 'T'  )
        {
            d = d+ str.charAt(0);
            str = str.substring(1,str.length());
        }
        if(str.length()>0 && str.charAt(0) == 'T')
        {
            str = str.substring(1,str.length());
        }

        if(str.length()>5)
            str =str.substring(0,str.length()-6);

        String t = str;


        txtHis_Date.setText(d);
        txtHis_Time.setText(t);




        return convertView;
    }
}