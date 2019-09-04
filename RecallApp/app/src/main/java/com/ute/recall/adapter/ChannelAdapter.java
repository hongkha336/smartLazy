package com.ute.recall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ute.recall.R;
import com.ute.recall.model.channelinfor;
import com.ute.recall.model.feeds;

import java.util.List;

public class ChannelAdapter  extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<channelinfor> channelinforList;


    public ChannelAdapter(Context context, int myLayout,  List<channelinfor> channelinforList)
    {
        this.myContext = context;
        this.myLayout = myLayout;
       this.channelinforList = channelinforList;
    }


    @Override
    public int getCount() {
        return channelinforList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout,null);
        // Ánh xạ và gán giá trị


        TextView txtChannelID = convertView.findViewById(R.id.txtchannelID);
        try {
            if(!channelinforList.get(position).getChannelName().toString().equals("null"))
            txtChannelID.setText(String.valueOf(channelinforList.get(position).getChannelName()).toString());
            else
            {
                txtChannelID.setText(String.valueOf(channelinforList.get(position).getChannelID()).toString());
            }
        }
        catch (Exception e)
        {
            txtChannelID.setText(String.valueOf(channelinforList.get(position).getChannelID()).toString());
        }

        return convertView;
    }
}
