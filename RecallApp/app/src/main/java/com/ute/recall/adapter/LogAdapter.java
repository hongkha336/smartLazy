package com.ute.recall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ute.recall.R;
import com.ute.recall.model.feeds;

import java.util.List;

public class LogAdapter extends BaseAdapter {


    Context myContext;
    int myLayout;
    List<feeds> feedsList;


    public LogAdapter(Context context, int myLayout, List<feeds> feedsList)
    {
        this.myContext = context;
        this.myLayout = myLayout;
        this.feedsList = feedsList;
    }

    @Override
    public int getCount() {
        return feedsList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout,null);
        // Ánh xạ và gán giá trị


        TextView txtTemp = convertView.findViewById(R.id.txtTemp);
        TextView txtAir = convertView.findViewById(R.id.txtAir);
        TextView txtSoil = convertView.findViewById(R.id.txtSoil);
        TextView txtTime = convertView.findViewById(R.id.txtTime);


        String air = feedsList.get(position).getField2();
        String temp = feedsList.get(position).getField1();
        String time = feedsList.get(position).getField4();
        String soil = feedsList.get(position).getField3();





        txtAir.setText(feedsList.get(position).getField2());
        txtTemp.setText(feedsList.get(position).getField1());
        txtTime.setText(feedsList.get(position).getField4());
        txtSoil.setText(feedsList.get(position).getField3());



//        TextView txtHis_Date = convertView.findViewById(R.id.txtHis_Time);
//        TextView txtHis_Time = convertView.findViewById(R.id.txtHis_Time2);
//        TextView txtHis_Temp = convertView.findViewById(R.id.txtHis_Temp);
//        TextView txtHis_Hum = convertView.findViewById(R.id.txtHis_Hum);
//        TextView txtHis_Soil = convertView.findViewById(R.id.txtHis_Soil);
//        TextView txtPump = convertView.findViewById(R.id.txtHis_Pump);
//
//        feeds history = feedsList.get(position);
//        txtPump.setText("Pump: " +history.getField4());
//        txtHis_Temp.setText("Temp: "+history.getField1());
//        txtHis_Hum.setText("Humi: "+history.getField2());
//        txtHis_Soil.setText("Soil: " +history.getField3());
//
//        String str = history.getCreated_at();
//        String d = "";
//        while(str.length() >0 &&str.charAt(0) != 'T'  )
//        {
//            d = d+ str.charAt(0);
//            str = str.substring(1,str.length());
//        }
//        if(str.length()>0 && str.charAt(0) == 'T')
//        {
//            str = str.substring(1,str.length());
//        }
//
//        if(str.length()>0)
//            str =str.substring(0,str.length()-1);
//
//        String t = str;
//
//
//        txtHis_Date.setText(d);
//        txtHis_Time.setText(t);

        return convertView;
    }
}
